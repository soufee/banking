package repository;

import dbutils.SqlHelper;
import entities.Account;
import entities.Client;
import entities.Operations;
import entities.enums.Sex;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class BaseTableTest extends RunListener {
    private SqlHelper helper = new SqlHelper("test");
    private AccountRepo accountRepo = new AccountRepo(helper);
    private ClientRepo clientRepo = new ClientRepo(helper);
    //    private TableOperations<Operations> operationsRepo = new OperationsRepo();
    private Client client1 = Client.builder().id(1L).firstName("Ivan").lastName("Ivanov").address("Russia").document("1616161616").isBlocked(false).sex(Sex.MALE).phone("+79041123232").build();
    private Client client2 = Client.builder().id(2L).firstName("Petra").lastName("Stankovic").address("Slovakia").document("1919191919").isBlocked(false).sex(Sex.FEMALE).phone("+7904000000").build();
    private Account account1 = Account.builder().accountNumber("111").amount(new BigDecimal(500.50)).currencyCode("RUB").owner(1L).isBlocked(false).build();
    private Account account2 = Account.builder().accountNumber("222").amount(new BigDecimal(1000.50)).currencyCode("RUB").owner(2L).isBlocked(false).build();
    private Account account3 = Account.builder().accountNumber("333").amount(new BigDecimal(800)).currencyCode("RUB").owner(2L).isBlocked(false).build();

//    @Before
//    public void setUp() throws Exception {
//        accountRepo.clear();
////        clientRepo.clear();
////        clientRepo.save(client1);
////        clientRepo.save(client2);
//        accountRepo.save(account1);
//        accountRepo.save(account2);
//    }

    @Test
    public void isRecordExist() {
        accountRepo.save(account1);
        accountRepo.save(account2);
        boolean recordExist1 = accountRepo.isRecordExist(account1, AccountRepo.TABLE_NAME);
        boolean recordExist2 = accountRepo.isRecordExist(account2, AccountRepo.TABLE_NAME);
        boolean recordExist3 = accountRepo.isRecordExist(account3, AccountRepo.TABLE_NAME);
        assertTrue(recordExist1);
        assertTrue(recordExist2);
        assertFalse(recordExist3);
        accountRepo.clear();
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        accountRepo.clear();
        clientRepo.clear();
    }
//
//    @Test
//    public void clear() {
//        assertTrue(true);
//    }
//
//    @Test
//    public void delete() {
//        assertTrue(true);
//    }
//
//    @Test
//    public void getAll() {
//        assertTrue(true);
//    }
//
//    @Test
//    public void size() {
//        assertTrue(true);
//    }
}