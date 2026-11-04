using Ewallet.Interfaces;
using Ewallet.Models;

namespace Ewallet.Repositories;

public class InMemoryTransactionRepository : ITransactionRepository
{
    private readonly List<Transaction> _store = new();

    public void Add(Transaction transaction) => _store.Add(transaction);

    public IReadOnlyList<Transaction> FindByAccountId(int accountId) =>
        _store
            .Where(t => t.Receiver.Id == accountId || t.Sender?.Id == accountId)
            .Reverse()
            .ToList();
}