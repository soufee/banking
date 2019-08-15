package repository;

import dbutils.SqlHelper;
import entities.Account;
import entities.Client;
import entities.enums.Sex;
import org.junit.*;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;

public class BaseTableTest {
    private static SqlHelper helper = new SqlHelper("test");
    private static AccountRepo accountRepo = new AccountRepo(helper);
    private ClientRepo clientRepo = new ClientRepo(helper);
    //    private TableOperations<Operations> operationsRepo = new OperationsRepo();
    private static Client client1 = Client.builder().id(1L).firstName("Ivan").lastName("Ivanov").address("Russia").document("1616161616").isBlocked(false).sex(Sex.MALE).phone("+79041123232").build();
    private static Client client2 = Client.builder().id(2L).firstName("Petra").lastName("Stankovic").address("Slovakia").document("1919191919").isBlocked(false).sex(Sex.FEMALE).phone("+7904000000").build();
    private static Account account1 = Account.builder().accountNumber("111").amount(new BigDecimal(500.50)).currencyCode("RUB").owner(1L).isBlocked(false).build();
    private static Account account2 = Account.builder().accountNumber("222").amount(new BigDecimal(1000.50)).currencyCode("RUB").owner(2L).isBlocked(false).build();
    private static Account account3 = Account.builder().accountNumber("333").amount(new BigDecimal(800)).currencyCode("RUB").owner(2L).isBlocked(false).build();

    // @BeforeClass
    public static void setUp() {
        accountRepo.save(account1);
        accountRepo.save(account2);
        accountRepo.save(account3);
    }

    @AfterClass
    public static void cleanUp() {
        accountRepo.clear();
    }


    @Test
    public void clear() {
        setUp();
        List<Account> all = accountRepo.getAll();
        assertTrue(all.size() == 3);
        accountRepo.clear();
        all = accountRepo.getAll();
        assertTrue(all.size() == 0);
    }

    @Test
    public void delete() {
        setUp();
        Account accountByAccountNumber = accountRepo.getAccountByAccountNumber("222");
        boolean delete = accountRepo.delete(accountByAccountNumber);
        assertTrue(delete);
        accountByAccountNumber = accountRepo.getAccountByAccountNumber("222");
        assertNull(accountByAccountNumber);
        accountByAccountNumber = accountRepo.getAccountByAccountNumber("111");
        boolean delete1 = accountRepo.delete(accountByAccountNumber.getId());
        assertTrue(delete1);
        accountByAccountNumber = accountRepo.getAccountByAccountNumber("111");
        assertNull(accountByAccountNumber);
        accountRepo.clear();
    }

    @Test
    public void getAll() {
        assertTrue(true);
    }

    @Test
    public void size() {
        assertTrue(true);
    }
}