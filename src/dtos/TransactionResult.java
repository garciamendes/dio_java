package dtos;

import Enums.STATUS_TRANSACTION;

public record TransactionResult(double amount, String message, STATUS_TRANSACTION type) {}
