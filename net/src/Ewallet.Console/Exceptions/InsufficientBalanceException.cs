namespace Ewallet.Console.Exceptions;

public class InsufficientBalanceException : WalletException
{
    public InsufficientBalanceException(long balance, long amount)
        : base($"Saldo tidak cukup. Saldo: {balance}, dibutuhkan: {amount}") { }
}