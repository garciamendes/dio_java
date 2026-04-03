package dtos;

import Enums.STATUS_TRANSACTION;
import Enums.TYPE_TRANSACTION;

import java.util.UUID;

public record Transaction(UUID uuid, TYPE_TRANSACTION type, double amount, STATUS_TRANSACTION status) {
}


