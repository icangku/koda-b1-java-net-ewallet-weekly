package dana.ewallet.repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import dana.ewallet.model.Account;

public class InMemoryAccountRepository implements AccountRepository {
  private final Map<Integer, Account> store = new LinkedHashMap<>();

  @Override
  public void save(Account a) {
    store.put(a.getId(), a);
  }

  @Override
  public Optional<Account> findById(int id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<Account> findAll() {
    return new ArrayList<>(store.values());
  }
}