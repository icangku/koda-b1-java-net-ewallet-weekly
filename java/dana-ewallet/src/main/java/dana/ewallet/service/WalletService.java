package dana.ewallet.service;

import java.util.List;

import dana.ewallet.model.Account;
import dana.ewallet.model.Wallet;

public interface WalletService {
  List<Account> getAccounts();

  Wallet getWallet(int accountId);

  Account findAccountByWalletNumber(String walletNumber);

  void topUp(int accountId, long amount);

  TransferResult transfer(int senderId, String destinationWalletNumber, long amount);

  List<HistoryEntry> getHistory(int accountId);

  long getTotalIncome(int accountId);

  long getTotalExpense(int accountId);
}
