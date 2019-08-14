package repository;

import dbutils.ExceptionUtil;
import dbutils.SqlHelper;
import entities.Account;
import entities.DBEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseTable {
    private SqlHelper helper;

    public BaseTable(SqlHelper helper) {
        this.helper = helper;
    }

    protected boolean isRecordExist(DBEntity t, String tableName) {
        DBEntity fromDb = helper.execute("SELECT * FROM " + tableName + " where ID = ?", ps -> {
            ps.setLong(1, t.getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                return null;
            }
            return Account.builder()
                    .id(rs.getLong("id"))
                    .build();
        });
        return fromDb != null;
    }

    public void clear(String tableName) {
        helper.execute("DELETE FROM " + tableName, PreparedStatement::execute);
    }

    public void delete(long id, String tableName) {
        helper.execute("DELETE FROM " + tableName + " WHERE id = ?", statement -> {
            statement.setLong(1, id);
            if (statement.executeUpdate() == 0) {
                throw new RuntimeException("The record with id " + id + " has not found in table " + tableName);
                //TODO log
            }
            return null;
        });
    }

    public List<DBEntity> getAll(String tableName) {
        return new ArrayList<>();
    }

    public int size(String tableName) {
        return helper.execute("SELECT count(*) FROM " + tableName, statement -> {
            ResultSet results = statement.executeQuery();
            return results.next() ? results.getInt(1) : 0;
        });
    }
}
