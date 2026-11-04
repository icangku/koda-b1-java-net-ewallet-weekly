// Services/TransferResult.cs
using Ewallet.Models;

namespace Ewallet.Services;

public record TransferResult(Account Sender, Account Receiver, long Amount, long SenderBalanceAfter);