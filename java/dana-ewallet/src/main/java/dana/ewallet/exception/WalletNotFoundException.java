package dana.ewallet.exception;

public class WalletNotFoundException extends WalletException {
  public WalletNotFoundException(String walletNumber) {
    super("Wallet " + walletNumber + " tidak ditemukan");
  }
}
