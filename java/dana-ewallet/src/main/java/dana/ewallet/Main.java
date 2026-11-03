package dana.ewallet;

import dana.ewallet.data.DummyData;
import dana.ewallet.exception.WalletException;
import dana.ewallet.model.Account;
import dana.ewallet.model.Wallet;
import dana.ewallet.repository.*;
import dana.ewallet.service.*;
import dana.ewallet.util.CurrencyFormatter;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner in = new Scanner(System.in);

    public static void main(String[] args) {
        AccountRepository accounts = new InMemoryAccountRepository();
        WalletRepository wallets = new InMemoryWalletRepository();
        TransactionRepository transactions = new InMemoryTransactionRepository();
        DummyData.seed(accounts, wallets, transactions);

        WalletService service = new WalletServiceImpl(accounts, wallets, transactions);

        while (true) {
            Account selected = chooseUser(service);
            if (selected == null)
                break;
            walletMenu(service, selected);
        }
        System.out.println("Terima kasih!");
    }

    private static Account chooseUser(WalletService service) {
        List<Account> list = service.getAccounts();
        System.out.println("\n========== KODA E-WALLET ==========");
        System.out.println("Pilih Pengguna:");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ". " + list.get(i).getName());
        }
        System.out.println((list.size() + 1) + ". Exit");

        int choice = readInt("Pilihan: ");
        if (choice >= 1 && choice <= list.size())
            return list.get(choice - 1);
        return null;
    }

    private static void walletMenu(WalletService service, Account user) {
        while (true) {
            System.out.println("\n========== WALLET " + user.getName().toUpperCase() + " ==========");
            System.out.println("1. Lihat Saldo\n2. Top Up\n3. Transfer\n4. Riwayat Transaksi\n5. Logout");
            int choice = readInt("Silahkan pilih menu: ");

            try {
                switch (choice) {
                    case 1 -> showBalance(service, user);
                    case 2 -> topUp(service, user);
                    case 3 -> transfer(service, user);
                    case 4 -> showHistory(service, user);
                    case 5 -> {
                        return;
                    }
                    default -> System.out.println("Menu tidak valid");
                }
            } catch (WalletException e) {
                System.out.println("Gagal: " + e.getMessage());
            }
        }
    }

    private static void showBalance(WalletService service, Account user) {
        Wallet w = service.getWallet(user.getId());
        System.out.println("Nomor Wallet : " + w.getWalletNumber());
        System.out.println("Nama Pemilik : " + user.getName());
        System.out.println("Saldo        : " + CurrencyFormatter.rupiah(w.getBalance()));
    }

    private static void topUp(WalletService service, Account user) {
        long amount = readLong("Nominal Top Up: ");
        service.topUp(user.getId(), amount);
        System.out.println("Top up berhasil! Saldo: "
                + CurrencyFormatter.rupiah(service.getWallet(user.getId()).getBalance()));
    }

    private static void transfer(WalletService service, Account user) {
        System.out.println("========== TRANSFER ==========");
        System.out.print("Nomor Wallet Tujuan: ");
        String number = in.nextLine().trim();

        Account receiver = service.findAccountByWalletNumber(number);
        System.out.println("Penerima            : " + receiver.getName());
        long amount = readLong("Nominal Transfer    : ");

        System.out.print("Konfirmasi Transfer (Y/n): ");
        if (!in.nextLine().trim().equalsIgnoreCase("Y")) {
            System.out.println("Transfer dibatalkan.");
            return;
        }

        TransferResult r = service.transfer(user.getId(), number, amount);
        System.out.println("Transfer berhasil!\n");
        System.out.println("Pengirim     : " + r.sender().getName());
        System.out.println("Penerima     : " + r.receiver().getName());
        System.out.println("Nominal      : " + CurrencyFormatter.rupiah(r.amount()));
        System.out.println("Sisa Saldo   : " + CurrencyFormatter.rupiah(r.senderBalanceAfter()));
    }

    private static void showHistory(WalletService service, Account user) {
        System.out.println("========== RIWAYAT ==========");
        List<HistoryEntry> history = service.getHistory(user.getId());
        if (history.isEmpty())
            System.out.println("(belum ada transaksi)");

        int no = 1;
        for (HistoryEntry e : history) {
            String label = switch (e.type()) {
                case TOP_UP -> "Top Up";
                case TRANSFER -> e.direction() == HistoryEntry.Direction.OUT
                        ? "Transfer Keluar"
                        : "Transfer Masuk";
            };
            String sign = e.direction() == HistoryEntry.Direction.OUT ? "-" : "+";
            System.out.printf("%d. %-16s %s %s%n", no++, label, sign, CurrencyFormatter.rupiah(e.amount()));
        }
        System.out.println("Total Pemasukan    : " + CurrencyFormatter.rupiah(service.getTotalIncome(user.getId())));
        System.out.println("Total Pengeluaran  : " + CurrencyFormatter.rupiah(service.getTotalExpense(user.getId())));
    }

    private static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(in.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static long readLong(String prompt) {
        System.out.print(prompt);
        try {
            return Long.parseLong(in.nextLine().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}