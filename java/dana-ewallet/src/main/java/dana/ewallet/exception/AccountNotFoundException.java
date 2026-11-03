package dana.ewallet.exception;

public class AccountNotFoundException extends WalletException {
  public AccountNotFoundException(int id) {
    super("Akun dengan id " + id + " tidak ditemukan");
  }
}