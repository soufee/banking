package repository;

import dbutils.SqlHelper;
import entities.DBEntity;
import entities.Operations;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

public class OperationsRepo extends BaseTable implements TableOperations<Operations> {
    private SqlHelper helper;
    private static final String TABLE_NAME = "OPERATIONS";

    public OperationsRepo(SqlHelper helper) {
        super(helper);
        this.helper = helper;
    }

    @Override
    protected List<DBEntity> getEntityFromResultSet(ResultSet rs) throws Exception {
        return null;
    }

    @Override
    public void clear() {
        super.clear(TABLE_NAME);
    }

    @Override
    public Operations update(Operations operations) {
        return null;
    }

    @Override
    public Operations save(Operations operations) {
        return null;
    }

    @Override
    public Operations get(Long id) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return super.delete(id, TABLE_NAME);
    }

    @Override
    public boolean delete(Operations operations) {
        return false;
    }

    @Override
    public List<Operations> getAll() {
        return super.getAll(TABLE_NAME).stream().map(s -> (Operations) s).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return super.size(TABLE_NAME);
    }
}
