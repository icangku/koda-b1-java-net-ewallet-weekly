using Ewallet.Models;

namespace Ewallet.Interfaces;

public interface IAccountRepository
{
    void Save(Account account);
    Account? FindById(int id);
    IReadOnlyList<Account> FindAll();
}