using Ewallet.Console.Models;

namespace Ewallet.Console.Interfaces;

public interface IAccountRepository
{
    void Save(Account account);
    Account? FindById(int id);
    IReadOnlyList<Account> FindAll();
}