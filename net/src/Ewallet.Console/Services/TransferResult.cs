// Services/TransferResult.cs
using Ewallet.Console.Models;

namespace Ewallet.Console.Services;

public record TransferResult(Account Sender, Account Receiver, long Amount, long SenderBalanceAfter);