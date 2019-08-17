package dbutils;

import entities.Account;
import entities.Client;
import entities.Operation;
import repository.*;

import javax.swing.filechooser.FileSystemView;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class DBConnector {

    public static void main(String[] args) {
        SqlHelper helper = new SqlHelper("dev");
        AccountRepoInterface accountRepo = new AccountRepo(helper);
        ClientRepoInterface clientRepo = new ClientRepo(helper);
        OperationRepoInterface operationRepo = new OperationRepo(helper);
        Client client1 = Client.builder().firstName("Ivan").lastName("Ivanov").address("Russia").document("1616161616").isBlocked(false).sex("MALE").phone("+79041123232").build();
        Client client2 = Client.builder().firstName("Petra").lastName("Stankovic").address("Slovakia").document("1919191919").isBlocked(false).sex("FEMALE").phone("+7904000000").build();
        Client client3 = Client.builder().firstName("Miroslava").lastName("Karpovic").address("Serbia").document("2000222111").isBlocked(false).sex("FEMALE").phone("+7904000003").build();
        Account account1 = Account.builder().accountNumber("111").amount(new BigDecimal(500.50)).currencyCode("RUB").owner(1L).isBlocked(false).build();
        Account account2 = Account.builder().accountNumber("222").amount(new BigDecimal(1000.50)).currencyCode("RUB").owner(2L).isBlocked(false).build();
        Account account3 = Account.builder().accountNumber("333").amount(new BigDecimal(800)).currencyCode("RUB").owner(3L).isBlocked(false).build();
        Account account4 = Account.builder().accountNumber("444").amount(new BigDecimal(350)).currencyCode("RUB").owner(1L).isBlocked(false).build();
        Account account5 = Account.builder().accountNumber("555").amount(new BigDecimal(400)).currencyCode("RUB").owner(2L).isBlocked(false).build();
        Account account6 = Account.builder().accountNumber("666").amount(new BigDecimal(950)).currencyCode("RUB").owner(3L).isBlocked(false).build();
        Operation operation1 = Operation.builder().from(account1.getAccountNumber()).to(account2.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(100)).dateTime(LocalDateTime.now()).build();
        Operation operation2 = Operation.builder().from(account3.getAccountNumber()).to(account1.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(50)).dateTime(LocalDateTime.now()).build();
        Operation operation3 = Operation.builder().from(account2.getAccountNumber()).to(account1.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(45)).dateTime(LocalDateTime.now()).build();
        Operation operation4 = Operation.builder().from(account1.getAccountNumber()).to(account3.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(350)).dateTime(LocalDateTime.now()).build();
        Operation operation5 = Operation.builder().from(account1.getAccountNumber()).to(account4.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(350)).dateTime(LocalDateTime.now()).build();
        Operation operation6 = Operation.builder().from(account4.getAccountNumber()).to(account5.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(350)).dateTime(LocalDateTime.now()).build();
        Operation operation7 = Operation.builder().from(account1.getAccountNumber()).to(account6.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(350)).dateTime(LocalDateTime.now()).build();
        Operation operation8 = Operation.builder().from(account6.getAccountNumber()).to(account3.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(350)).dateTime(LocalDateTime.now()).build();
//        accountRepo.save(account4);
//        accountRepo.save(account5);
//        accountRepo.save(account6);
//        clientRepo.save(client1);
//        clientRepo.save(client2);
//        clientRepo.save(client3);

        operationRepo.save(operation5);
        operationRepo.save(operation6);
        operationRepo.save(operation7);
        operationRepo.save(operation8);

    }
}
