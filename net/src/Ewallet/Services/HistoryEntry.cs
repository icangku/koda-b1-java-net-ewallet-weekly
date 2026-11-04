using Ewallet.Models;

namespace Ewallet.Services;

public enum Direction { In, Out }

public record HistoryEntry(Direction Direction, TransactionType Type, string CounterpartyName, long Amount);