namespace Ewallet.Console.Models;

public class Account
{
    public int Id { get; }
    public string Name { get; }

    public Account(int id, string name)
    {
        if (string.IsNullOrWhiteSpace(name))
            throw new ArgumentException("Nama wajib diisi", nameof(name));

        Id = id;
        Name = name;
    }
}