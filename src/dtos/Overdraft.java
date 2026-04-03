package dtos;

public record Overdraft(String cpf, Double currentLimit, Double outstandingBalance) {
    public Overdraft registerCurrentLimit(double limit) {
        return new Overdraft(cpf, limit, outstandingBalance);
    }

    public Overdraft registerWithdraw(User user, Double value) {
        if (value <= 0 || value > currentLimit) {
            return this;
        }

        var currentValue = currentLimit - value;
        var outstandingBalanceCurrent = outstandingBalance + (value + (value * .2));
        return new Overdraft(
                this.cpf,
                currentValue,
                outstandingBalanceCurrent
        );
    }

}
