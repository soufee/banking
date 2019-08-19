package repository;

import com.google.common.base.Preconditions;
import dbutils.SqlHelper;
import entities.Account;
import entities.Client;
import entities.DBEntity;
import lombok.extern.log4j.Log4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
public class AccountRepo extends BaseTable implements AccountRepoInterface {
    private SqlHelper helper;
    public static final String TABLE_NAME = "ACCOUNTS";

    public AccountRepo(SqlHelper helper) {
        super(helper);
        this.helper = helper;
    }

    @Override
    public void clear() {
        log.debug("Clearing the table " + TABLE_NAME);
        super.clear(TABLE_NAME);
    }

    @Override
    public Account update(Account account) {
        Preconditions.checkNotNull(account);
        log.debug("Updating account " + account.getAccountNumber());
        Account toUpdate = getAccountByAccountNumber(account.getAccountNumber());
        if (toUpdate != null) {
            return helper.execute("UPDATE " + TABLE_NAME + " SET amount = ?, isblocked = ? where id = ?", ps -> {
                ps.setBigDecimal(1, account.getAmount());
                ps.setBoolean(2, account.isBlocked());
                ps.setLong(3, account.getId());
                if (ps.executeUpdate() != 1) {
                    log.warn("Exception while updating the record " + account.getAccountNumber() + " in " + TABLE_NAME);
                    throw new RuntimeException("Exception while updating the Account record with id " + account.getId());
                }
                return getAccountByAccountNumber(account.getAccountNumber());
            });
        } else {
            log.debug("Could not update. The record " + account.getId() + " does not exist in table " + TABLE_NAME);
            return save(account);
        }
    }

    @Override
    public Account save(Account account) {
        Preconditions.checkNotNull(account);
        log.debug("Saving account " + account.getAccountNumber());
        Account byAccountNumber = getAccountByAccountNumber(account.getAccountNumber());
        if (byAccountNumber != null) {
            log.debug("The account " + account.getAccountNumber() + " already exists...");
            return update(byAccountNumber);
        } else {
            Account acc = helper.transactionalExecute(conn -> {
                PreparedStatement statement = conn.prepareStatement(
                        "INSERT INTO " + TABLE_NAME + " (ACCOUNT_NUMBER, CURRENCY_CODE, AMOUNT, OWNER, ISBLOCKED)" +
                                " VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, account.getAccountNumber());
                statement.setString(2, account.getCurrencyCode());
                statement.setBigDecimal(3, account.getAmount());
                statement.setLong(4, account.getOwner());
                statement.setBoolean(5, account.isBlocked());
                statement.execute();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    account.setId(id);
                    log.debug("Saved account with id " + id);
                }
                return account;
            });
            return getAccountByAccountNumber(acc.getAccountNumber());
        }

    }

    @Override
    public Account get(Long id) {
        log.debug("Get Account " + id);
        return (Account) super.get(id, TABLE_NAME);
    }

    @Override
    public boolean delete(Account account) {
        Preconditions.checkNotNull(account);
        log.debug("Deleting account " + account.getAccountNumber());
        Account accountByAccountNumber = getAccountByAccountNumber(account.getAccountNumber());
        if (accountByAccountNumber != null)
            return super.delete(accountByAccountNumber.getId(), TABLE_NAME);
        else return false;
    }

    @Override
    public List<Account> getAll() {
        log.debug("Get All records in table " + TABLE_NAME);
        return super.getAll(TABLE_NAME).stream().map(s -> (Account) s).collect(Collectors.toList());
    }

    @Override
    public int size() {
        int size = super.size(TABLE_NAME);
        log.debug("Geting size of table = " + size);
        return size;
    }

    @Override
    public List<Account> getAccountsByClient(Client client) {
        log.debug("Get All accounts of Client " + client.getFirstName() + " " + client.getLastName());
        return getAccountsByClientId(client.getId());
    }

    private List<Account> getAccountsByClientId(Long id) {
        log.debug("Get All accounts of Client with ID " + id);
        return getAll().stream().filter(s -> s.getOwner().equals(id)).collect(Collectors.toList());

    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        Preconditions.checkState(accountNumber != null && !"".equals(accountNumber), "Account number must not be null or empty");
        return getAll().stream().filter(s -> s.getAccountNumber().equals(accountNumber)).findFirst().orElse(null);
    }

    @Override
    protected List<DBEntity> getEntityFromResultSet(ResultSet rs) throws SQLException {
        log.debug("Geting list of Accounts by ResultSet ");
        List<DBEntity> list = new ArrayList<>();
        while (rs.next()) {
            list.add(Account.builder()
                    .id(rs.getLong("id"))
                    .accountNumber(rs.getString("account_number"))
                    .currencyCode(rs.getString("currency_code"))
                    .amount(rs.getBigDecimal("amount"))
                    .owner(rs.getLong("owner"))
                    .isBlocked(rs.getBoolean("isblocked"))
                    .build());
        }
        log.debug("Size of list got from ResultSet =  " + list.size());
        return list;
    }

}
