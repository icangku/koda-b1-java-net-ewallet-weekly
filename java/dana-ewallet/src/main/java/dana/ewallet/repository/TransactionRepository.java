package dana.ewallet.repository;

import java.util.List;

import dana.ewallet.model.Transaction;

public interface TransactionRepository {
  void add(Transaction transaction);

  List<Transaction> findByAccountId(int accountId);
}
