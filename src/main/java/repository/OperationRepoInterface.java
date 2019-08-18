package repository;

import entities.Account;
import entities.Operation;

import java.time.LocalDate;
import java.util.List;

public interface OperationRepoInterface extends TableOperations<Operation> {
    List<Operation> getAllOperationsForDate(LocalDate date);
    List<Operation> getAllIncomeOperations(Account account);
    List<Operation> getAllOutcomeOperations(Account account);
}
