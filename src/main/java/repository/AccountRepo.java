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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
public class AccountRepo extends BaseTable implements TableOperations<Account> {
    private SqlHelper helper;
    public static final String TABLE_NAME = "ACCOUNTS";

    public AccountRepo(SqlHelper helper) {
        super(helper);
        this.helper = helper;
    }

    @Override
    public void clear() {
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
                    log.debug("Exception while updating the record " + account.getAccountNumber() + " in " + TABLE_NAME);
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
                                " VALUES (?, ?, ?, ?, ?)");
                statement.setString(1, account.getAccountNumber());
                statement.setString(2, account.getCurrencyCode());
                statement.setBigDecimal(3, account.getAmount());
                statement.setLong(4, account.getOwner());
                statement.setBoolean(5, account.isBlocked());
                statement.execute();
                return account;
            });
            return getAccountByAccountNumber(acc.getAccountNumber());
        }

    }

    @Override
    public Account get(Long id) {
        return helper.execute("select * from " + TABLE_NAME + " where id = ?", statement -> {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<DBEntity> entityFromResultSet = getEntityFromResultSet(rs);
            if (entityFromResultSet.size() == 1)
                return (Account) entityFromResultSet.get(0);
            else {
                log.warn("Query has got wrong answer " + entityFromResultSet.size());
                return null;
            }
        });
    }

    @Override
    public boolean delete(Account account) {
        Preconditions.checkNotNull(account);
        log.debug("Deleting account " + account.getAccountNumber());
        return delete(account.getId());
    }

    @Override
    public boolean delete(Long id) {
        return super.delete(id, TABLE_NAME);
    }

    @Override
    public List<Account> getAll() {
        return super.getAll(TABLE_NAME).stream().map(s -> (Account) s).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return super.size(TABLE_NAME);
    }

    public List<Account> getAccountsByClient(Client client) {
        return getAccountsByClientId(client.getId());
    }

    public List<Account> getAccountsByClientId(Long id) {
        return null;

    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return helper.execute("SELECT * FROM " + TABLE_NAME + " where ACCOUNT_NUMBER = ?", statement -> {
            statement.setString(1, accountNumber);
            ResultSet rs = statement.executeQuery();
            List<DBEntity> entityFromResultSet = getEntityFromResultSet(rs);
            if (entityFromResultSet.size() == 1)
                return (Account) entityFromResultSet.get(0);
            else {
                log.error("Query has got wrong answer " + entityFromResultSet.size());
                return null;
            }
        });

    }

    @Override
    protected List<DBEntity> getEntityFromResultSet(ResultSet rs) throws SQLException {
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
        return list;

    }

}
