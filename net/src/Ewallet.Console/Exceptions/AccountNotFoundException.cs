namespace Ewallet.Console.Exceptions;

public class AccountNotFoundException : WalletException
{
    public AccountNotFoundException(int id) : base($"Akun dengan id {id} tidak ditemukan") { }
}