package dana.ewallet.model;

import java.util.Objects;

import dana.ewallet.exception.InsufficientBalanceException;
import dana.ewallet.exception.InvalidAmountException;

public class Wallet {
  private final Account user;
  private long balance;

  public Wallet(Account user, long initialBalance) {
    this.user = Objects.requireNonNull(user, "Wallet harus dimiliki sebuah akun");
    if (initialBalance < 0)
      throw new InvalidAmountException("Saldo awal tidak boleh negatif");
    this.balance = initialBalance;
  }

  public Account getUser() {
    return user;
  }

  public long getBalance() {
    return balance;
  }

  public String getWalletNumber() {
    return String.format("WAL%03d", user.getId());
  }

  public void ensureCanDeposit(long amount) {
    requirePositive(amount);
    if (balance > Long.MAX_VALUE - amount) {
      throw new InvalidAmountException("Saldo melebihi batas maksimum");
    }
  }

  public void ensureCanWithdraw(long amount) {
    requirePositive(amount);
    if (amount > balance)
      throw new InsufficientBalanceException(balance, amount);
  }

  public void deposit(long amount) {
    ensureCanDeposit(amount);
    balance += amount;
  }

  public void withdraw(long amount) {
    ensureCanWithdraw(amount);
    balance -= amount;
  }

  private static void requirePositive(long amount) {
    if (amount <= 0)
      throw new InvalidAmountException("Nominal harus lebih dari 0");
  }
}