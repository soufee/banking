package service;

import dbutils.SqlHelper;
import entities.Account;
import entities.Operation;
import exceptions.TransactionException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j;
import repository.AccountRepoInterface;
import repository.OperationRepoInterface;

import java.math.BigDecimal;

@Log4j
@AllArgsConstructor
public class TransferService {
    private SqlHelper sqlHelper;
    private OperationRepoInterface operRepo;
    private AccountRepoInterface accRepo;

    public synchronized void rollBack(Operation operation, Account from, Account to) {
        try {
            if (operation.getId() != null) {
                operRepo.delete(operation);
            }
            if (from != null) {
                from.setAmount(from.getAmount().add(operation.getAmount()));
                accRepo.update(from);
            }
            if (to != null) {
                to.setAmount(to.getAmount().subtract(operation.getAmount()));
                accRepo.save(to);
            }
        } catch (Exception e) {
            log.error("Could not rollback operation " + operation);
            throw e;
        }

    }

    public synchronized String doTransaction(Operation operation) {
        BigDecimal amount = operation.getAmount();
        Account accountFrom = accRepo.getAccountByAccountNumber(operation.getFrom());
        if (accountFrom.getAmount().compareTo(amount) < 0) {
            throw new TransactionException("Not enough money on account " + accountFrom.getAccountNumber());
        }
        Account accountTo = accRepo.getAccountByAccountNumber(operation.getTo());
        accountFrom.setAmount(accountFrom.getAmount().subtract(amount));
        accountTo.setAmount(accountTo.getAmount().add(amount));
        saveOperationAndAccounts(operation, accountFrom, accountTo);
        return "operation saved succesfully " + operation;
    }

    private synchronized void saveOperationAndAccounts(Operation operation, Account accountFrom, Account accountTo) {
        Account accountFrom2 = null;
        Account accountTo2 = null;
        try {
            operation = operRepo.save(operation);
            accountFrom2 = accRepo.update(accountFrom);
            accountTo2 = accRepo.update(accountTo);
        } catch (Exception e) {
            rollBack(operation, accountFrom2, accountTo2);
            throw e;
        }
    }
}
