package service;

import entities.Account;
import entities.Operation;
import exceptions.TransactionException;
import org.junit.BeforeClass;
import org.junit.Test;
import repository.BaseTableTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class TransferServiceTest extends BaseTableTest {
    TransferService service = new TransferService(helper, operationRepo, accountRepo);

    @BeforeClass
    public static void setUp() {
        accountRepo.save(account1);
        accountRepo.save(account2);
        accountRepo.save(account3);
        operationRepo.save(operation1);
        operationRepo.save(operation2);
        operationRepo.save(operation3);
        operationRepo.save(operation4);
    }

    @Test
    public void rollBack() {
        Account accountFrom = Account.builder()
                .accountNumber("654")
                .amount(BigDecimal.valueOf(100))
                .currencyCode("RUB")
                .owner(1L)
                .isBlocked(false)
                .build();
        Account accountTo = Account.builder()
                .accountNumber("655")
                .amount(BigDecimal.valueOf(100))
                .currencyCode("RUB")
                .owner(1L)
                .isBlocked(false)
                .build();
        Account save1 = accountRepo.save(accountFrom);
        Account save2 = accountRepo.save(accountTo);
        Operation operation = Operation.builder()
                .from(save1.getAccountNumber())
                .to(save2.getAccountNumber())
                .amount(BigDecimal.valueOf(50))
                .currency("RUB")
                .dateTime(LocalDateTime.now())
                .build();
        Operation save = operationRepo.save(operation);

        assertNotNull(save);
        assertNotNull(save.getId());
        assertNotNull(save1);
        assertNotNull(save1.getId());
        assertNotNull(save2);
        assertNotNull(save2.getId());
        service.rollBack(save, accountFrom, accountTo);
        assertNull(operationRepo.get(save.getId()));
        Account toCheckFrom = accountRepo.get(save1.getId());
        Account toCheckTo = accountRepo.get(save2.getId());
        assertEquals(toCheckFrom.getAmount(), BigDecimal.valueOf(150));
        assertEquals(toCheckTo.getAmount(), BigDecimal.valueOf(50));
    }

    @Test(expected = TransactionException.class)
    public void doTransactionWithException() {
        Operation operation = Operation.builder()
                .from(account2.getAccountNumber())
                .to(account3.getAccountNumber())
                .amount(BigDecimal.valueOf(10000))
                .currency("RUB")
                .dateTime(LocalDateTime.now())
                .build();
        service.doTransaction(operation);

    }

    @Test
    public void doTransactionCorrect() {
        Operation operation = Operation.builder()
                .from(account1.getAccountNumber())
                .to(account2.getAccountNumber())
                .amount(BigDecimal.valueOf(100))
                .currency("RUB")
                .dateTime(LocalDateTime.now())
                .build();
        String response = service.doTransaction(operation);
        assertTrue(response.contains("operation saved succesfully"));
        List<Operation> allOperationsForDate = operationRepo.getAllOperationsForDate(LocalDateTime.now().toLocalDate());
        assertTrue(allOperationsForDate.contains(operation));
        assertTrue(true);
    }
}