package repository;

import dbutils.SqlHelper;
import entities.*;
import org.junit.*;


import java.math.BigDecimal;
import java.time.LocalDateTime;

public class BaseTableTest {
    public static SqlHelper helper = new SqlHelper("test");
    public static AccountRepoInterface accountRepo = new AccountRepo(helper);
    public static ClientRepoInterface clientRepo = new ClientRepo(helper);
    public static OperationRepoInterface operationRepo = new OperationRepo(helper);
    public static Client client1 = Client.builder().id(1L).firstName("Ivan").lastName("Ivanov").address("Russia").document("1616161616").isBlocked(false).sex("MALE").phone("+79041123232").build();
    public static Client client2 = Client.builder().id(2L).firstName("Petra").lastName("Stankovic").address("Slovakia").document("1919191919").isBlocked(false).sex("FEMALE").phone("+7904000000").build();
    public static Client client3 = Client.builder().id(3L).firstName("Miroslava").lastName("Karpovic").address("Serbia").document("2000222111").isBlocked(false).sex("FEMALE").phone("+7904000003").build();
    public static Account account1 = Account.builder().accountNumber("111").amount(new BigDecimal(500.50)).currencyCode("RUB").owner(1L).isBlocked(false).build();
    public static Account account2 = Account.builder().accountNumber("222").amount(new BigDecimal(1000.50)).currencyCode("RUB").owner(2L).isBlocked(false).build();
    public static Account account3 = Account.builder().accountNumber("333").amount(new BigDecimal(800)).currencyCode("RUB").owner(3L).isBlocked(false).build();
    public static Operation operation1 = Operation.builder().from(account1.getAccountNumber()).to(account2.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(100)).dateTime(LocalDateTime.now()).build();
    public static Operation operation2 = Operation.builder().from(account3.getAccountNumber()).to(account1.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(50)).dateTime(LocalDateTime.now()).build();
    public static Operation operation3 = Operation.builder().from(account2.getAccountNumber()).to(account1.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(45)).dateTime(LocalDateTime.now()).build();
    public static Operation operation4 = Operation.builder().from(account1.getAccountNumber()).to(account3.getAccountNumber()).currency("RUB").amount(BigDecimal.valueOf(350)).dateTime(LocalDateTime.now()).build();

    @AfterClass
    public static void cleanUp() {
        accountRepo.clear();
        clientRepo.clear();
        operationRepo.clear();
    }


}