package repository;

import entities.Account;
import lombok.extern.log4j.Log4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Log4j
public class AccountRepoTest extends BaseTableTest {

    @Before
    public  void setUp() {
        accountRepo.save(account1);
        accountRepo.save(account2);
        accountRepo.save(account3);
    }

    @After
    public  void clearData() {
        accountRepo.clear();
    }

    @Test
    public void clear() {
        log.debug("Clear...");
        List<Account> all = accountRepo.getAll();
        log.debug("Clear: all.size = "+all.size());
        assertEquals(3, all.size());
        accountRepo.clear();
        all = accountRepo.getAll();
        log.debug("Clear: all.size = "+all.size());
        assertEquals(0, all.size());
    }

    @Test
    public void delete() {
        log.debug("Delete...");
        Account accountByAccountNumber = accountRepo.getAccountByAccountNumber("222");
        boolean delete = accountRepo.delete(accountByAccountNumber);
        assertTrue(delete);
        accountByAccountNumber = accountRepo.getAccountByAccountNumber("222");
        assertNull(accountByAccountNumber);
    }

    @Test
    public void getAll() {
        log.debug("getAll...");
        List<Account> all = accountRepo.getAll();
        assertTrue(all.contains(account1));
        assertTrue(all.contains(account2));
        assertTrue(all.contains(account3));
    }

    @Test
    public void size() {
        log.debug("size...");
        List<Account> all = accountRepo.getAll();
        assertEquals(all.size(), 3);
    }

    @Test
    public void get() {
        log.debug("get...");
        Account accountByAccountNumber = accountRepo.getAccountByAccountNumber(account1.getAccountNumber());
        Account byId = accountRepo.get(accountByAccountNumber.getId());
        assertEquals(accountByAccountNumber, byId);
    }


    @Test
    public void update() {
        log.debug("update...");
        Account acc = accountRepo.getAccountByAccountNumber(account3.getAccountNumber());
        assertEquals(account3.getAmount(), acc.getAmount());
        acc.setAmount(acc.getAmount().add(BigDecimal.valueOf(350)));
        Account update = accountRepo.update(acc);
        assertEquals(update.getAmount(), account3.getAmount().add(BigDecimal.valueOf(350)));
    }

    @Test
    public void save() {
        log.debug("save...");
        Account acc = Account.builder()
                .owner(1L)
                .accountNumber("865")
                .currencyCode("RUB")
                .amount(BigDecimal.valueOf(0))
                .build();
        Account saved = accountRepo.save(acc);
        assertEquals(saved.getAmount(), BigDecimal.valueOf(0));
        assertFalse(saved.isBlocked());
        assertEquals((long) saved.getOwner(), 1L);
        assertEquals(saved.getAccountNumber(), "865");
        assertEquals(saved.getCurrencyCode(), "RUB");
    }

    @Test
    public void getAccountsByClient() {
        log.debug("getAccountsByClient...");
        List<Account> accountsByClient = accountRepo.getAccountsByClient(client1);
        assertTrue(accountsByClient.contains(account1));
        assertFalse(accountsByClient.contains(account2));
        assertFalse(accountsByClient.contains(account3));
    }

    @Test
    public void getAccountByAccountNumber() {
        log.debug("getAccountByAccountNumber...");
        Account accountByAccountNumber = accountRepo.getAccountByAccountNumber("222");
        assertEquals(accountByAccountNumber, account2);
    }
}