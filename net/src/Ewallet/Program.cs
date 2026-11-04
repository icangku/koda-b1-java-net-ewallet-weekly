// Program.cs
using Ewallet.Data;
using Ewallet.Exceptions;
using Ewallet.Interfaces;
using Ewallet.Models;
using Ewallet.Repositories;
using Ewallet.Services;
using Ewallet.Utils;

namespace Ewallet;

internal static class Program
{
    private static void Main()
    {
        IAccountRepository accounts = new InMemoryAccountRepository();
        IWalletRepository wallets = new InMemoryWalletRepository();
        ITransactionRepository transactions = new InMemoryTransactionRepository();
        DummyData.Seed(accounts, wallets, transactions);

        IWalletService service = new WalletService(accounts, wallets, transactions);

        while (true)
        {
            var selected = ChooseUser(service);
            if (selected is null) break;
            WalletMenu(service, selected);
        }

        Console.WriteLine("Terima kasih!");
    }

    private static Account? ChooseUser(IWalletService service)
    {
        var list = service.GetAccounts();
        Console.WriteLine("\n========== DANA E-WALLET ==========");
        Console.WriteLine("Pilih Pengguna:");
        for (int i = 0; i < list.Count; i++)
            Console.WriteLine($"{i + 1}. {list[i].Name}");
        Console.WriteLine($"{list.Count + 1}. Exit");

        int choice = ReadInt("Pilihan: ");
        return choice >= 1 && choice <= list.Count ? list[choice - 1] : null;
    }

    private static void WalletMenu(IWalletService service, Account user)
    {
        while (true)
        {
            Console.WriteLine($"\n========== WALLET {user.Name.ToUpper()} ==========");
            Console.WriteLine("1. Lihat Saldo\n2. Top Up\n3. Transfer\n4. Riwayat Transaksi\n5. Logout");
            int choice = ReadInt("Silahkan pilih menu: ");

            try
            {
                switch (choice)
                {
                    case 1: ShowBalance(service, user); break;
                    case 2: TopUp(service, user); break;
                    case 3: Transfer(service, user); break;
                    case 4: ShowHistory(service, user); break;
                    case 5: return;
                    default: Console.WriteLine("Menu tidak valid"); break;
                }
            }
            catch (WalletException e)
            {
                Console.WriteLine($"Gagal: {e.Message}");
            }
        }
    }


    private static void ShowBalance(IWalletService service, Account user)
    {
        var w = service.GetWallet(user.Id);
        Console.WriteLine($"Nomor Wallet : {w.WalletNumber}");
        Console.WriteLine($"Nama Pemilik : {user.Name}");
        Console.WriteLine($"Saldo        : {CurrencyFormatter.Rupiah(w.Balance)}");
    }

    private static void TopUp(IWalletService service, Account user)
    {
        long amount = ReadLong("Nominal Top Up: ");
        service.TopUp(user.Id, amount);
        Console.WriteLine($"Top up berhasil! Saldo: {CurrencyFormatter.Rupiah(service.GetWallet(user.Id).Balance)}");
    }

    private static void Transfer(IWalletService service, Account user)
    {
        Console.WriteLine("========== TRANSFER ==========");
        Console.Write("Nomor Wallet Tujuan: ");
        string number = (Console.ReadLine() ?? "").Trim();

        var receiver = service.FindAccountByWalletNumber(number);
        Console.WriteLine($"Penerima            : {receiver.Name}");
        long amount = ReadLong("Nominal Transfer    : ");

        Console.Write("Konfirmasi Transfer (Y/n): ");
        if (!string.Equals((Console.ReadLine() ?? "").Trim(), "Y", StringComparison.OrdinalIgnoreCase))
        {
            Console.WriteLine("Transfer dibatalkan.");
            return;
        }

        var r = service.Transfer(user.Id, number, amount);
        Console.WriteLine("Transfer berhasil!\n");
        Console.WriteLine($"Pengirim     : {r.Sender.Name}");
        Console.WriteLine($"Penerima     : {r.Receiver.Name}");
        Console.WriteLine($"Nominal      : {CurrencyFormatter.Rupiah(r.Amount)}");
        Console.WriteLine($"Sisa Saldo   : {CurrencyFormatter.Rupiah(r.SenderBalanceAfter)}");
    }

    private static void ShowHistory(IWalletService service, Account user)
    {
        Console.WriteLine("========== RIWAYAT ==========");
        var history = service.GetHistory(user.Id);
        if (history.Count == 0) Console.WriteLine("(belum ada transaksi)");

        int no = 1;
        foreach (var e in history)
        {
            string label = e.Type switch
            {
                TransactionType.TopUp => "Top Up",
                TransactionType.Transfer => e.Direction == Direction.Out ? "Transfer Keluar" : "Transfer Masuk",
                _ => "-"
            };
            string sign = e.Direction == Direction.Out ? "-" : "+";
            Console.WriteLine($"{no++}. {label,-16} {sign} {CurrencyFormatter.Rupiah(e.Amount)}");
        }

        Console.WriteLine($"Total Pemasukan    : {CurrencyFormatter.Rupiah(service.GetTotalIncome(user.Id))}");
        Console.WriteLine($"Total Pengeluaran  : {CurrencyFormatter.Rupiah(service.GetTotalExpense(user.Id))}");
    }


    private static int ReadInt(string prompt)
    {
        Console.Write(prompt);
        return int.TryParse(Console.ReadLine(), out var v) ? v : -1;
    }

    private static long ReadLong(string prompt)
    {
        Console.Write(prompt);
        return long.TryParse(Console.ReadLine(), out var v) ? v : 0;
    }
}