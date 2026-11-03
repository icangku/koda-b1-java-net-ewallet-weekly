package dana.ewallet.data;

import dana.ewallet.model.*;
import dana.ewallet.repository.*;

public final class DummyData {
  private DummyData() {
  }

  public static void seed(AccountRepository accounts, WalletRepository wallets,
      TransactionRepository transactions) {
    Account andi = new Account(1, "Andi");
    Account budi = new Account(2, "Budi");
    Account citra = new Account(3, "Citra");

    for (Account a : new Account[] { andi, budi, citra })
      accounts.save(a);

    wallets.save(new Wallet(andi, 500_000));
    wallets.save(new Wallet(budi, 250_000));
    wallets.save(new Wallet(citra, 750_000));

    transactions.add(Transaction.topUp(andi, 200_000));
    transactions.add(Transaction.transfer(citra, budi, 50_000));
  }
}