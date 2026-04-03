package Enums;

public enum STATUS_TRANSACTION {
    SUCCESS("Sucesso"),
    FAILURE("Falhou"),
    PENDING("Aguardando");

    private final String label;
    private STATUS_TRANSACTION(String type) {
        this.label = type;
    }

    public String getLabel() {
        return this.label;
    }
}
