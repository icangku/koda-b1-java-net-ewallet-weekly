using Ewallet.Console.Models;

namespace Ewallet.Console.Services;

public enum Direction { In, Out }

public record HistoryEntry(Direction Direction, TransactionType Type, string CounterpartyName, long Amount);