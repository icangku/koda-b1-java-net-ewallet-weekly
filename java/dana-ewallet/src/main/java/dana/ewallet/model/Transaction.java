package dana.ewallet.model;

import java.util.Objects;
import dana.ewallet.exception.InvalidAmountException;

public class Transaction {
  private final Account sender;
  private final Account receiver;
  private final long amount;
  private final TransactionType type;

  private Transaction(Account sender, Account receiver, long amount, TransactionType type) {
    if (amount <= 0)
      throw new InvalidAmountException("Nominal harus lebih dari 0");
    this.sender = sender;
    this.receiver = Objects.requireNonNull(receiver, "Penerima wajib berupa akun");
    this.amount = amount;
    this.type = type;
  }

  public static Transaction transfer(Account sender, Account receiver, long amount) {
    Objects.requireNonNull(sender, "Pengirim wajib berupa akun");
    return new Transaction(sender, receiver, amount, TransactionType.TRANSFER);
  }

  public static Transaction topUp(Account account, long amount) {
    return new Transaction(null, account, amount, TransactionType.TOP_UP);
  }

  public Account getSender() {
    return sender;
  }

  public Account getReceiver() {
    return receiver;
  }

  public long getAmount() {
    return amount;
  }

  public TransactionType getType() {
    return type;
  }
}
