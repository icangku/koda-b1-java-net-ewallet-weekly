namespace Ewallet.Console.Exceptions;

public class SelfTransferException : WalletException
{
    public SelfTransferException() : base("Tidak bisa transfer ke wallet sendiri") { }
}