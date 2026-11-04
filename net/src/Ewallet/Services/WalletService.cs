using Ewallet.Exceptions;
using Ewallet.Interfaces;
using Ewallet.Models;

namespace Ewallet.Services;

public class WalletService : IWalletService
{
    private readonly IAccountRepository _accounts;
    private readonly IWalletRepository _wallets;
    private readonly ITransactionRepository _transactions;

    public WalletService(IAccountRepository accounts, IWalletRepository wallets,
                         ITransactionRepository transactions)
    {
        _accounts = accounts;
        _wallets = wallets;
        _transactions = transactions;
    }

    public IReadOnlyList<Account> GetAccounts() => _accounts.FindAll();

    public Wallet GetWallet(int accountId) =>
        _wallets.FindByAccountId(accountId) ?? throw new AccountNotFoundException(accountId);

    public Account FindAccountByWalletNumber(string walletNumber) =>
        (_wallets.FindByWalletNumber(walletNumber)
            ?? throw new WalletNotFoundException(walletNumber)).User;

    public void TopUp(int accountId, long amount)
    {
        var wallet = GetWallet(accountId);
        wallet.EnsureCanDeposit(amount);   // validate first
        wallet.Deposit(amount);            // then mutate
        _transactions.Add(Transaction.TopUp(wallet.User, amount));
    }

    public TransferResult Transfer(int senderId, string destinationWalletNumber, long amount)
    {
        var from = GetWallet(senderId);
        var to = _wallets.FindByWalletNumber(destinationWalletNumber)
                 ?? throw new WalletNotFoundException(destinationWalletNumber);

        // Phase 1: validate EVERYTHING. No data has changed yet.
        if (from.User.Id == to.User.Id) throw new SelfTransferException();
        from.EnsureCanWithdraw(amount);
        to.EnsureCanDeposit(amount);

        // Phase 2: mutate. Every precondition passed, so this cannot fail halfway.
        from.Withdraw(amount);
        to.Deposit(amount);
        _transactions.Add(Transaction.Transfer(from.User, to.User, amount));

        return new TransferResult(from.User, to.User, amount, from.Balance);
    }

    public IReadOnlyList<HistoryEntry> GetHistory(int accountId) =>
        _transactions.FindByAccountId(accountId)
            .Select(t => ToEntry(t, accountId))
            .ToList();

    public long GetTotalIncome(int accountId) => Sum(accountId, Direction.In);

    public long GetTotalExpense(int accountId) => Sum(accountId, Direction.Out);

    private long Sum(int accountId, Direction direction) =>
        GetHistory(accountId).Where(e => e.Direction == direction).Sum(e => e.Amount);

    private static HistoryEntry ToEntry(Transaction t, int accountId)
    {
        bool outgoing = t.Type == TransactionType.Transfer && t.Sender!.Id == accountId;
        if (outgoing)
            return new HistoryEntry(Direction.Out, t.Type, t.Receiver.Name, t.Amount);

        return new HistoryEntry(Direction.In, t.Type, t.Sender?.Name ?? "-", t.Amount);
    }
}