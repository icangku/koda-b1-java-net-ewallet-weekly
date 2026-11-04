using Ewallet.Interfaces;
using Ewallet.Models;

namespace Ewallet.Repositories;

public class InMemoryWalletRepository : IWalletRepository
{
    private readonly Dictionary<int, Wallet> _store = new();

    public void Save(Wallet wallet) => _store[wallet.User.Id] = wallet;

    public Wallet? FindByAccountId(int accountId) => _store.GetValueOrDefault(accountId);

    public Wallet? FindByWalletNumber(string walletNumber) =>
        _store.Values.FirstOrDefault(w =>
            string.Equals(w.WalletNumber, walletNumber, StringComparison.OrdinalIgnoreCase));
}