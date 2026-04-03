package entities;

import Enums.STATUS_TRANSACTION;
import Enums.TYPE_TRANSACTION;
import dtos.*;

import java.util.*;

public class Bank {
    private final Map<String, Double> wallet = new HashMap<String, Double>();
    private final Map<String, UserDocumentDTO> userDocument = new  HashMap<String, UserDocumentDTO>();
    private final Map<String, List<Transaction>> transaction = new HashMap<String, List<Transaction>>();
    private final Map<String, Overdraft> overdraft= new HashMap<>();

    public Bank(User user) {
        this.wallet.computeIfAbsent(user.cpf(), k -> 0.0);
        this.userDocument.computeIfAbsent(user.cpf(), k -> new UserDocumentDTO(user.cpf(), false, 0.0));
        this.transaction.computeIfAbsent(user.cpf(), k -> new  ArrayList<>());
        this.overdraft.computeIfAbsent(user.cpf(), k -> new Overdraft(user.cpf(), 0.0, 0.0));
    }

    public Double getMoney(User user) {
        if (!this.wallet.containsKey(user.cpf())) {
            return  0.0;
        }

        return this.wallet.get(user.cpf());
    }

    public List<Transaction> getExtract(User user) {
        return this.transaction.getOrDefault(user.cpf(), List.of());
    }

    public Double getOutstandingBalance(User user) {
        return this.overdraft.get(user.cpf()).outstandingBalance();
    }

    public Double getOverdraftLimit(User user) {
        return this.userDocument.get(user.cpf()).fullLimitReleased();
    }

    public TransactionResult deposit(User user, Double amount) {
        if (this.transaction.get(user.cpf()).isEmpty() && !this.userDocument.get(user.cpf()).hasMadeFirstDeposit()) {
            this.userDocument.computeIfPresent(user.cpf(), (key, document) ->
                document.withOverdraftFirstDeposit(true, null)
            );

            var document = this.userDocument.get(user.cpf());
            this.overdraft.computeIfPresent(user.cpf(), (cpf, overdraft) ->
                    overdraft.registerCurrentLimit(document.fullLimitReleased()));
        }

        if (amount == null || amount.isNaN() || amount <= 0) {
            Transaction transaction = new Transaction(
                    UUID.randomUUID(),
                    TYPE_TRANSACTION.DEPOSIT,
                    amount == null ? 0.0 : amount,
                    STATUS_TRANSACTION.FAILURE
            );
            this.transaction.get(user.cpf()).add(transaction);
            return new TransactionResult(0.0, "Verifique o valor depositado", STATUS_TRANSACTION.FAILURE);
        }

        if (!this.wallet.containsKey(user.cpf())) {
            Transaction transaction = new Transaction(
                    UUID.randomUUID(),
                    TYPE_TRANSACTION.DEPOSIT,
                    amount,
                    STATUS_TRANSACTION.FAILURE
            );
            this.transaction.get(user.cpf()).add(transaction);
            return new TransactionResult(0.0, "Você precisa criar uma conta para movimentações",  STATUS_TRANSACTION.FAILURE);
        }

        var currentAmount = this.wallet.get(user.cpf());
        var totalToDeposit = currentAmount + amount;
        this.wallet.put(user.cpf(), totalToDeposit);

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                TYPE_TRANSACTION.DEPOSIT,
                amount,
                STATUS_TRANSACTION.SUCCESS
        );
        this.transaction.get(user.cpf()).add(transaction);

        this.userDocument.computeIfPresent(user.cpf(), (cpf, document) ->
                document.applyOverdraftLimit(amount)
        );

        var valueLimit = this.userDocument.get(user.cpf()).fullLimitReleased();
        this.overdraft.computeIfPresent(user.cpf(), (cpf,  overdraft) ->
                overdraft.registerCurrentLimit(valueLimit)
        );

        return new TransactionResult(amount, "Valor depositado", STATUS_TRANSACTION.SUCCESS);
    }

    public TransactionResult withdraw(User user, double amount) {
        var currentWallet = this.wallet.get(user.cpf());

        if (currentWallet == null || currentWallet <= 0 || currentWallet < amount) {
            Transaction transaction = new Transaction(
                    UUID.randomUUID(),
                    TYPE_TRANSACTION.WITHDRAW,
                    amount,
                    STATUS_TRANSACTION.FAILURE
            );
            this.transaction.get(user.cpf()).add(transaction);
            return new TransactionResult(amount, "Saldo insuficiente ou sem saldo", STATUS_TRANSACTION.FAILURE);
        }

        this.wallet.put(user.cpf(), currentWallet - amount);

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                TYPE_TRANSACTION.WITHDRAW,
                amount,
                STATUS_TRANSACTION.SUCCESS
        );
        this.transaction.get(user.cpf()).add(transaction);
        return new TransactionResult(amount, "Valor retirado", STATUS_TRANSACTION.SUCCESS);
    }

    public TransactionResult withdrawOverdraft(User user, double amount) {
        var currentOverdraft = this.overdraft.get(user.cpf());

        if (currentOverdraft.currentLimit() <= 0 || currentOverdraft.currentLimit() < amount) {
            Transaction transaction = new Transaction(
                    UUID.randomUUID(),
                    TYPE_TRANSACTION.OVERDRAFT,
                    amount,
                    STATUS_TRANSACTION.FAILURE
            );
            this.transaction.get(user.cpf()).add(transaction);
            return new TransactionResult(amount, "Saldo insuficiente ou sem saldo no cheque especial", STATUS_TRANSACTION.FAILURE);
        }

        this.overdraft.computeIfPresent(user.cpf(), (cpf, overdraft) ->
                overdraft.registerWithdraw(user,  amount));

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                TYPE_TRANSACTION.OVERDRAFT,
                amount,
                STATUS_TRANSACTION.SUCCESS
        );
        this.transaction.get(user.cpf()).add(transaction);
        return new TransactionResult(amount, "Valor retirado", STATUS_TRANSACTION.SUCCESS);
    }
}
