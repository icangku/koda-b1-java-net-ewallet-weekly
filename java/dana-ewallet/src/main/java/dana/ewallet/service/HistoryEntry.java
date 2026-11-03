package dana.ewallet.service;

import dana.ewallet.model.TransactionType;

public record HistoryEntry(Direction direction, TransactionType type, String counterpartyName, long amount) {
  public enum Direction {
    IN, OUT
  }
}