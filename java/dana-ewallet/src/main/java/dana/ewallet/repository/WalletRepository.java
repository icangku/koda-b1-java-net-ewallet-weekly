package dana.ewallet.repository;

import java.util.Optional;
import dana.ewallet.model.Wallet;

public interface WalletRepository {
  void save(Wallet wallet);

  Optional<Wallet> findByAccountId(int accountId);

  Optional<Wallet> findByWalletNumber(String walletNumber);
}