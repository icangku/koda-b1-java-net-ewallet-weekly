namespace Ewallet.Exceptions;

public class InvalidAmountException : WalletException
{
    public InvalidAmountException(string message) : base(message) { }
}