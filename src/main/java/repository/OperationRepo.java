package repository;

import com.google.common.base.Preconditions;
import dbutils.SqlHelper;
import entities.Account;
import entities.DBEntity;
import entities.Operation;
import lombok.extern.log4j.Log4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
public class OperationRepo extends BaseTable implements OperationRepoInterface {
    private SqlHelper helper;
    private static final String TABLE_NAME = "OPERATIONS";

    public OperationRepo(SqlHelper helper) {
        super(helper);
        this.helper = helper;
    }

    @Override
    public void clear() {
        super.clear(TABLE_NAME);
    }

    @Override
    public Operation update(Operation operation) {
        log.error("Not permitted to change operations.");
        throw new IllegalStateException("Not permitted to change/update operations.");
    }

    @Override
    public Operation save(Operation operation) {
        Preconditions.checkNotNull(operation);
        log.debug("Saving operation " + operation);
        Operation gotOperation = get(operation.getId());
        if (gotOperation != null) {
            log.debug("The operation " + operation.getId() + " already exists...");
            return operation;
        } else {
            return helper.transactionalExecute(conn -> {
                PreparedStatement statement = conn.prepareStatement(
                        "INSERT INTO " + TABLE_NAME + " (from_acc, TO_ACC, amount, CURRENCY, DATE_TIME)" +
                                " VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, operation.getFrom());
                statement.setString(2, operation.getTo());
                statement.setBigDecimal(3, operation.getAmount());
                statement.setString(4, operation.getCurrency());
                statement.setTimestamp(5, Timestamp.valueOf(operation.getDateTime()));
                statement.execute();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    operation.setId(id);
                    return operation;
                } else {
                    return null;
                }
            });
        }
    }

    @Override
    public Operation get(Long id) {
        log.debug("Get Client " + id);
        return (Operation) super.get(id, TABLE_NAME);
    }

    @Override
    public boolean delete(Operation operation) {
        return !super.delete(operation.getId(), TABLE_NAME);
    }

    @Override
    public List<Operation> getAll() {
        return super.getAll(TABLE_NAME).stream().map(s -> (Operation) s).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return super.size(TABLE_NAME);
    }

    @Override
    protected List<DBEntity> getEntityFromResultSet(ResultSet rs) throws Exception {
        log.debug("Geting list of Operation by ResultSet ");
        List<DBEntity> list = new ArrayList<>();
        while (rs.next()) {
            list.add(Operation.builder()
                    .id(rs.getLong("id"))
                    .from(rs.getString("from_acc"))
                    .to(rs.getString("to_acc"))
                    .amount(rs.getBigDecimal("amount"))
                    .dateTime(getLocalDateTimeFromTimeStamp(rs.getTimestamp("DATE_TIME")))
                    .currency(rs.getString("currency"))
                    .build());
        }
        log.debug("Size of list got from ResultSet =  " + list.size());
        return list;
    }

    @Override
    public List<Operation> getAllOperationsForDate(LocalDate date) {
        return getAll().stream().filter(s -> s.getDateTime().toLocalDate().equals(date)).collect(Collectors.toList());
    }

    @Override
    public List<Operation> getAllIncomeOperations(Account account) {
        return getAll().stream().filter(s -> s.getTo().equals(account.getAccountNumber())).collect(Collectors.toList());
    }

    @Override
    public List<Operation> getAllOutcomeOperations(Account account) {
        return getAll().stream().filter(s -> s.getFrom().equals(account.getAccountNumber())).collect(Collectors.toList());
    }

    private LocalDateTime getLocalDateTimeFromTimeStamp(Timestamp dateTime) {
        return dateTime.toLocalDateTime();
    }
}
