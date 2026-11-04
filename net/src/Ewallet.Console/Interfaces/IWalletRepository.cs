using Ewallet.Console.Models;

namespace Ewallet.Console.Interfaces;

public interface IWalletRepository
{
    void Save(Wallet wallet);
    Wallet? FindByAccountId(int accountId);
    Wallet? FindByWalletNumber(string walletNumber);
}