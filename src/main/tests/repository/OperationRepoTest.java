package repository;

import entities.Operation;
import org.junit.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class OperationRepoTest extends BaseTableTest {

    @BeforeClass
    public static void setUp() {
        operation1 = operationRepo.save(operation1);
        operation2 = operationRepo.save(operation2);
        operation3 = operationRepo.save(operation3);
        operation4 = operationRepo.save(operation4);
        assertNotNull(operation1);
        assertNotNull(operation2);
        assertNotNull(operation3);
        assertNotNull(operation4);
    }

    @AfterClass
    public static void clearData() {
        operationRepo.clear();
        assertEquals(operationRepo.size(), 0);

    }

//    @Test
//    public void clear() {
//
//    }

    @Test(expected = IllegalStateException.class)
    public void update() {
        Operation operation = operationRepo.getAllIncomeOperations(account1).get(0);
        operation.setAmount(operation.getAmount().add(BigDecimal.valueOf(1000)));
        operationRepo.update(operation);
    }

    @Test
    public void save() {
        Operation op = Operation.builder()
                .from(account2.getAccountNumber())
                .to(account3.getAccountNumber())
                .amount(BigDecimal.TEN)
                .currency("RUB")
                .dateTime(LocalDateTime.now())
                .build();
        operationRepo.save(op);
        List<Operation> allOperationsForDate = operationRepo.getAllOperationsForDate(LocalDateTime.now().toLocalDate());
        assertTrue(allOperationsForDate.contains(op));
    }

    @Test
    public void get() {
        Operation operation = operationRepo.get(operation1.getId()); //TODO разобраться с get by ID
        assertNotNull(operation);
        List<Operation> all = operationRepo.getAll();
        assertTrue(all.contains(operation1));
    }

    @Test
    public void delete() {
        operationRepo.delete(operation4);
        List<Operation> all = operationRepo.getAll();
        assertFalse(all.contains(operation4));
    }

    @Test
    public void getAll() {
        List<Operation> all = operationRepo.getAll();
        assertNotNull(all);
        assertTrue(all.size() != 0);
    }

    @Test
    public void size() {
        assertTrue(operationRepo.size()>0);
    }

    @Test
    public void getAllOperationsForDate() {
        final LocalDate today = LocalDateTime.now().toLocalDate();
        List<Operation> allOperationsForDate = operationRepo.getAllOperationsForDate(today);
        assertNotNull(allOperationsForDate);
        for (Operation operation : allOperationsForDate) {
            assertEquals(operation.getDateTime().toLocalDate(), today);
        }
    }

    @Test
    public void getAllIncomeOperations() {
        List<Operation> allIncomeOperations = operationRepo.getAllIncomeOperations(account1);
        for (Operation operation: allIncomeOperations) {
            assertEquals(account1.getAccountNumber(), operation.getTo());
        }
    }

    @Test
    public void getAllOutcomeOperations() {
        List<Operation> allOutcomeOperations = operationRepo.getAllOutcomeOperations(account1);
        for (Operation operation: allOutcomeOperations) {
            assertEquals(account1.getAccountNumber(), operation.getFrom());
        }
    }
}