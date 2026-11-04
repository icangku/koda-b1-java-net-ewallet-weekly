using Ewallet.Console.Interfaces;
using Ewallet.Console.Models;

namespace Ewallet.Console.Repositories;

public class InMemoryAccountRepository : IAccountRepository
{
    private readonly Dictionary<int, Account> _store = new();

    public void Save(Account account) => _store[account.Id] = account;

    public Account? FindById(int id) => _store.GetValueOrDefault(id);

    public IReadOnlyList<Account> FindAll() => _store.Values.ToList();
}