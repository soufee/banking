package repository;

import dbutils.SqlHelper;
import entities.Account;
import entities.Client;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

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
    public void update(Account account) {
        if (isRecordExist(account, TABLE_NAME)) {
            helper.execute("UPDATE " + TABLE_NAME + " SET amount = ?, isblocked = ? where id = ?", ps -> {
                ps.setBigDecimal(1, account.getAmount());
                ps.setBoolean(2, account.isBlocked());
                ps.setLong(3, account.getId());
                if (ps.executeUpdate() != 1) {
                    throw new RuntimeException("Exception while updating the Account record with id " + account.getId());
                }
                return null;
            });

        } else {
            //TODO лог
            save(account);
        }
    }

    @Override
    public void save(Account account) {
        helper.transactionalExecute(conn -> {
            try (PreparedStatement statement = conn.prepareStatement(
                    "INSERT INTO " + TABLE_NAME + " (ACCOUNT_NUMBER, CURRENCY_CODE, AMOUNT, OWNER, ISBLOCKED)" +
                            " VALUES (?, ?, ?, ?, ?)")) {
                statement.setString(1, account.getAccountNumber());
                statement.setString(2, account.getCurrencyCode());
                statement.setBigDecimal(3, account.getAmount());
                statement.setLong(4, account.getOwner());
                statement.setBoolean(5, account.isBlocked());
                statement.execute();
            }
            return null;
        });
    }

    @Override
    public Account get(long id) {
        return helper.execute("select * from " + TABLE_NAME + " where id = ?", statement -> {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            if (!rs.next()) {
                throw new RuntimeException("Couldn't find a record with id = " + id + " in the table " + TABLE_NAME);
            }
            return Account.builder()
                    .id(rs.getLong("id"))
                    .accountNumber(rs.getString("account_number"))
                    .currencyCode(rs.getString("currency_code"))
                    .amount(rs.getBigDecimal("amount"))
                    .owner(rs.getLong("owner"))
                    .isBlocked(rs.getBoolean("isblocked"))
                    .build();

        });
    }

    @Override
    public void delete(long id) {
        super.delete(id, TABLE_NAME);
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

    public List<Account> getAccountsByClientId(long id) {
        return null;

    }

    public Account getAccountByAccountNumber(String accountNumber) {
        return null;
    }
}
