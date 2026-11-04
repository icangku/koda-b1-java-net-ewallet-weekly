using Ewallet.Console.Models;
using Ewallet.Console.Services;

namespace Ewallet.Console.Interfaces;

public interface IWalletService
{
    IReadOnlyList<Account> GetAccounts();
    Wallet GetWallet(int accountId);
    Account FindAccountByWalletNumber(string walletNumber);
    void TopUp(int accountId, long amount);
    TransferResult Transfer(int senderId, string destinationWalletNumber, long amount);
    IReadOnlyList<HistoryEntry> GetHistory(int accountId);
    long GetTotalIncome(int accountId);
    long GetTotalExpense(int accountId);
}