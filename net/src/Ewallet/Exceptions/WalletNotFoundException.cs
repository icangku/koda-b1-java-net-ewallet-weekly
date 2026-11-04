namespace Ewallet.Exceptions;

public class WalletNotFoundException : WalletException
{
  public WalletNotFoundException(string walletNumber) : base($"Wallet {walletNumber} tidak ditemukan") { }
}