import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import repository.AccountRepoTest;
import repository.ClientRepoTest;
import repository.OperationRepoTest;
import service.ApiTest;
import service.TransferServiceTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountRepoTest.class,
        ClientRepoTest.class,
        OperationRepoTest.class,
        TransferServiceTest.class,
        ApiTest.class
})

public class AllTests {
}
