package dana.ewallet.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import dana.ewallet.model.Wallet;

public class InMemoryWalletRepository implements WalletRepository {
  private final Map<Integer, Wallet> store = new HashMap<>();

  @Override
  public void save(Wallet w) {
    store.put(w.getUser().getId(), w);
  }

  @Override
  public Optional<Wallet> findByAccountId(int id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public Optional<Wallet> findByWalletNumber(String number) {
    return store.values().stream()
        .filter(w -> w.getWalletNumber().equalsIgnoreCase(number))
        .findFirst();
  }
}