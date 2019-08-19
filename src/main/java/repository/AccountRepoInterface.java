package repository;

import entities.Account;
import entities.Client;

import java.util.List;

public interface AccountRepoInterface extends TableOperations<Account> {

    List<Account> getAccountsByClient(Client client);

    Account getAccountByAccountNumber(String accountNumber);
}
