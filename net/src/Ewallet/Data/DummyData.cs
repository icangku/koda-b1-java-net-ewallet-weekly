using Ewallet.Interfaces;
using Ewallet.Models;

namespace Ewallet.Data;

public static class DummyData
{
    public static void Seed(IAccountRepository accounts, IWalletRepository wallets,
                            ITransactionRepository transactions)
    {
        var andi = new Account(1, "Andi");
        var budi = new Account(2, "Budi");
        var citra = new Account(3, "Citra");

        foreach (var a in new[] { andi, budi, citra }) accounts.Save(a);

        wallets.Save(new Wallet(andi, 500_000));
        wallets.Save(new Wallet(budi, 250_000));
        wallets.Save(new Wallet(citra, 750_000));

        transactions.Add(Transaction.TopUp(andi, 200_000));
        transactions.Add(Transaction.Transfer(citra, budi, 50_000));
    }
}