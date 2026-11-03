package dana.ewallet.exception;

public class InsufficientBalanceException extends WalletException {
  public InsufficientBalanceException(long balance, long amount) {
    super("Saldo tidak cukup. Saldo: " + balance + ", dibutuhkan: " + amount);
  }
}