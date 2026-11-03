package dana.ewallet.service;

import dana.ewallet.model.Account;

public record TransferResult(Account sender, Account receiver, long amount, long senderBalanceAfter) {
}
