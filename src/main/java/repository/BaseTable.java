package repository;

import dbutils.SqlHelper;
import entities.DBEntity;
import lombok.extern.log4j.Log4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Log4j
public abstract class BaseTable {
    private SqlHelper helper;

    public BaseTable(SqlHelper helper) {
        this.helper = helper;
    }

    protected void clear(String tableName) {
        helper.execute("DELETE FROM " + tableName, PreparedStatement::execute);
    }

    protected boolean delete(long id, String tableName) {
        helper.execute("DELETE FROM " + tableName + " WHERE id = ?", statement -> {
            statement.setLong(1, id);
            if (statement.executeUpdate() == 0) {
                throw new RuntimeException("The record with id " + id + " has not found in table " + tableName);
                //TODO log
            }
            return null;
        });
        return false;
    }

    protected List<DBEntity> getAll(String tableName) {
        return helper.execute("SELECT * FROM " + tableName, s -> {
            ResultSet resultSet = s.executeQuery();
            return getEntityFromResultSet(resultSet);
        });
    }

    protected int size(String tableName) {
        return helper.execute("SELECT count(*) FROM " + tableName, statement -> {
            ResultSet results = statement.executeQuery();
            return results.next() ? results.getInt(1) : 0;
        });
    }

    protected abstract List<DBEntity> getEntityFromResultSet(ResultSet rs) throws Exception;
}
