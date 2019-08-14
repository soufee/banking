package repository;

import dbutils.SqlHelper;
import entities.Operations;

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
    public void clear() {
        super.clear(TABLE_NAME);
    }

    @Override
    public void update(Operations operations) {

    }

    @Override
    public void save(Operations operations) {

    }

    @Override
    public Operations get(long id) {
        return null;
    }

    @Override
    public void delete(long id) {
        super.delete(id, TABLE_NAME);
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
