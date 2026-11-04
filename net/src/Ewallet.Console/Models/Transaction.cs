// Models/Transaction.cs
using Ewallet.Console.Exceptions;

namespace Ewallet.Console.Models;

public class Transaction
{
    public Account? Sender { get; }
    public Account Receiver { get; }
    public long Amount { get; }
    public TransactionType Type { get; }

    private Transaction(Account? sender, Account receiver, long amount, TransactionType type)
    {
        if (amount <= 0)
            throw new InvalidAmountException("Nominal harus lebih dari 0");

        Sender = sender;
        Receiver = receiver ?? throw new ArgumentNullException(nameof(receiver), "Penerima wajib berupa akun");
        Amount = amount;
        Type = type;
    }

    public static Transaction Transfer(Account sender, Account receiver, long amount)
    {
        ArgumentNullException.ThrowIfNull(sender, nameof(sender));
        return new Transaction(sender, receiver, amount, TransactionType.Transfer);
    }

    public static Transaction TopUp(Account account, long amount)
        => new(null, account, amount, TransactionType.TopUp);
}