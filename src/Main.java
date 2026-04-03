import dtos.Transaction;
import dtos.User;
import entities.Bank;

public class Main {

    public static void main(String[] args) {
        User user = new User("Matheus", "1234567891011");
        Bank bank = new Bank(user);

        var depositResult = bank.deposit(user, Double.parseDouble("521"));

        switch (depositResult.type()) {
            case FAILURE ->  {
                System.out.println(depositResult.message());
            }
            case SUCCESS -> System.out.println(depositResult.message() + ": " + depositResult.amount());
            default -> {}
        }

        System.out.println("Limite disponível de cheque especial: " + bank.getOverdraftLimit(user));;

        var withdrawResult = bank.withdraw(user, Double.parseDouble("130"));
        switch (withdrawResult.type()) {
            case FAILURE -> System.out.println(withdrawResult.message());
            case SUCCESS -> System.out.println(withdrawResult.message() + ": " + withdrawResult.amount());
        }

        bank.withdrawOverdraft(user,  Double.parseDouble("270"));
        System.out.println("Saldo devedor do cheque especial: " + bank.getOutstandingBalance(user));

        var transactionsResult = bank.getExtract(user);

        System.out.println("===============");
        for (Transaction transaction : transactionsResult) {
            System.out.println("--------------");
            System.out.println("Tipo: " + transaction.type().getLabel());
            System.out.println("Valor: R$ " + transaction.amount());
            System.out.println("Status: " + transaction.status().getLabel());
            System.out.println("--------------");
        }
        System.out.println("===============");
    }
}