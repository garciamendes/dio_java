package dtos;

public record UserDocumentDTO(String cpf, Boolean hasMadeFirstDeposit, Double fullLimitReleased) {

    public UserDocumentDTO withOverdraftFirstDeposit(boolean value, Double overdraftLimit) {
        var limit = overdraftLimit != null ? overdraftLimit : this.fullLimitReleased;
        return new UserDocumentDTO(
                this.cpf,
                value,
                limit
        );
    }

    public UserDocumentDTO applyOverdraftLimit(double amount) {
        if (!this.hasMadeFirstDeposit) {
            return this;
        }

        if (amount <= 500) {
            return this.withOverdraftFirstDeposit(false, 50.00);
        }

        if (amount > 500) {
            var limit = amount / 2;
            return this.withOverdraftFirstDeposit(true, limit);
        }

        return this;
    }
}
