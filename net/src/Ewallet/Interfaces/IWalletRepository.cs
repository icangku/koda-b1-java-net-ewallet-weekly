using Ewallet.Models;

namespace Ewallet.Interfaces;

public interface IWalletRepository
{
    void Save(Wallet wallet);
    Wallet? FindByAccountId(int accountId);
    Wallet? FindByWalletNumber(string walletNumber);
}