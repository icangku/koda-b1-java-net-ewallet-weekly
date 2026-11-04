using Ewallet.Console.Models;

namespace Ewallet.Console.Interfaces;

public interface ITransactionRepository
{
    void Add(Transaction transaction);
    IReadOnlyList<Transaction> FindByAccountId(int accountId); // newest first
}