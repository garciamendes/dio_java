package Enums;

public enum TYPE_TRANSACTION {
    DEPOSIT("Depósito"),
    WITHDRAW("Saque"),
    OVERDRAFT("Cheque especial");

    private final String label;

    private TYPE_TRANSACTION(String type) {
        this.label = type;
    }

    public String getLabel() {
        return this.label;
    }
}
