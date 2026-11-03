package dana.ewallet.repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dana.ewallet.model.Transaction;

public class InMemoryTransactionRepository implements TransactionRepository {
  private final List<Transaction> store = new ArrayList<>();

  @Override
  public void add(Transaction t) {
    store.add(t);
  }

  @Override
  public List<Transaction> findByAccountId(int accountId) {
    List<Transaction> result = new ArrayList<>();
    for (Transaction t : store) {
      boolean isReceiver = t.getReceiver().getId() == accountId;
      boolean isSender = t.getSender() != null && t.getSender().getId() == accountId;
      if (isReceiver || isSender)
        result.add(t);
    }
    Collections.reverse(result);
    return result;
  }
}
