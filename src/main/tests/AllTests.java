import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import repository.AccountRepoTest;
import repository.BaseTableTest;
import repository.ClientRepoTest;
import repository.OperationRepoTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        AccountRepoTest.class,
        ClientRepoTest.class,
        OperationRepoTest.class
})

public class AllTests {
}
