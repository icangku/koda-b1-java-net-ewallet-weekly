using Ewallet.Models;

namespace Ewallet.Interfaces;

public interface ITransactionRepository
{
    void Add(Transaction transaction);
    IReadOnlyList<Transaction> FindByAccountId(int accountId); // newest first
}