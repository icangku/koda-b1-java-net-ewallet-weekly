package dana.ewallet.service;

import dana.ewallet.exception.*;
import dana.ewallet.model.*;
import dana.ewallet.repository.*;
import java.util.List;

public class WalletServiceImpl implements WalletService {
  private final AccountRepository accounts;
  private final WalletRepository wallets;
  private final TransactionRepository transactions;

  public WalletServiceImpl(AccountRepository accounts, WalletRepository wallets,
      TransactionRepository transactions) {
    this.accounts = accounts;
    this.wallets = wallets;
    this.transactions = transactions;
  }

  @Override
  public List<Account> getAccounts() {
    return accounts.findAll();
  }

  @Override
  public Wallet getWallet(int accountId) {
    return wallets.findByAccountId(accountId)
        .orElseThrow(() -> new AccountNotFoundException(accountId));
  }

  @Override
  public Account findAccountByWalletNumber(String walletNumber) {
    return wallets.findByWalletNumber(walletNumber)
        .orElseThrow(() -> new WalletNotFoundException(walletNumber))
        .getUser();
  }

  @Override
  public void topUp(int accountId, long amount) {
    Wallet wallet = getWallet(accountId);
    wallet.ensureCanDeposit(amount);
    wallet.deposit(amount);
    transactions.add(Transaction.topUp(wallet.getUser(), amount));
  }

  @Override
  public TransferResult transfer(int senderId, String destinationWalletNumber, long amount) {
    Wallet from = getWallet(senderId);
    Wallet to = wallets.findByWalletNumber(destinationWalletNumber)
        .orElseThrow(() -> new WalletNotFoundException(destinationWalletNumber));

    if (from.getUser().getId() == to.getUser().getId())
      throw new SelfTransferException();
    from.ensureCanWithdraw(amount);
    to.ensureCanDeposit(amount);

    from.withdraw(amount);
    to.deposit(amount);
    transactions.add(Transaction.transfer(from.getUser(), to.getUser(), amount));

    return new TransferResult(from.getUser(), to.getUser(), amount, from.getBalance());
  }

  @Override
  public List<HistoryEntry> getHistory(int accountId) {
    return transactions.findByAccountId(accountId).stream()
        .map(t -> toEntry(t, accountId))
        .toList();
  }

  @Override
  public long getTotalIncome(int accountId) {
    return sum(accountId, HistoryEntry.Direction.IN);
  }

  @Override
  public long getTotalExpense(int accountId) {
    return sum(accountId, HistoryEntry.Direction.OUT);
  }

  private long sum(int accountId, HistoryEntry.Direction direction) {
    return getHistory(accountId).stream()
        .filter(e -> e.direction() == direction)
        .mapToLong(HistoryEntry::amount)
        .sum();
  }

  private HistoryEntry toEntry(Transaction t, int accountId) {
    boolean outgoing = t.getType() == TransactionType.TRANSFER
        && t.getSender().getId() == accountId;
    if (outgoing) {
      return new HistoryEntry(HistoryEntry.Direction.OUT, t.getType(),
          t.getReceiver().getName(), t.getAmount());
    }
    String counterparty = t.getSender() == null ? "-" : t.getSender().getName();
    return new HistoryEntry(HistoryEntry.Direction.IN, t.getType(), counterparty, t.getAmount());
  }
}