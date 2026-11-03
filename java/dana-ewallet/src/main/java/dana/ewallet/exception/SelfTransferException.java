package dana.ewallet.exception;

public class SelfTransferException extends WalletException {
  public SelfTransferException() {
    super("Tidak bisa transfer ke wallet sendiri");
  }
}