package dana.ewallet.repository;

import java.util.List;
import java.util.Optional;

import dana.ewallet.model.Account;

public interface AccountRepository {
  void save(Account account);

  Optional<Account> findById(int id);

  List<Account> findAll();
}