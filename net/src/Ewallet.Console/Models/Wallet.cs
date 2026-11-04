using Ewallet.Console.Exceptions;

namespace Ewallet.Console.Models;

public class Wallet
{
    public Account User { get; }
    public long Balance { get; private set; }

    public string WalletNumber => $"WAL{User.Id:D3}";

    public Wallet(Account user, long initialBalance)
    {
        User = user ?? throw new ArgumentNullException(nameof(user), "Wallet harus dimiliki sebuah akun");
        if (initialBalance < 0)
            throw new InvalidAmountException("Saldo awal tidak boleh negatif");
        Balance = initialBalance;
    }

    public void EnsureCanDeposit(long amount)
    {
        RequirePositive(amount);
        if (Balance > long.MaxValue - amount)
            throw new InvalidAmountException("Saldo melebihi batas maksimum");
    }

    public void EnsureCanWithdraw(long amount)
    {
        RequirePositive(amount);
        if (amount > Balance)
            throw new InsufficientBalanceException(Balance, amount);
    }

    public void Deposit(long amount)
    {
        EnsureCanDeposit(amount);
        Balance += amount;
    }

    public void Withdraw(long amount)
    {
        EnsureCanWithdraw(amount);
        Balance -= amount;
    }

    private static void RequirePositive(long amount)
    {
        if (amount <= 0)
            throw new InvalidAmountException("Nominal harus lebih dari 0");
    }
}