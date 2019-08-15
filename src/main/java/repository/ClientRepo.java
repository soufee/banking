package repository;

import dbutils.SqlHelper;
import entities.Account;
import entities.Client;
import entities.DBEntity;

import java.sql.ResultSet;
import java.util.List;
import java.util.stream.Collectors;

public class ClientRepo extends BaseTable implements TableOperations<Client> {
    private SqlHelper helper;
    private static final String TABLE_NAME = "CLIENTS";

    public ClientRepo(SqlHelper helper) {
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
    public Client update(Client client) {
        return null;
    }

    @Override
    public Client save(Client client) {
        return null;
    }

    @Override
    public Client get(Long id) {
        return null;
    }


    @Override
    public boolean delete(Long id) {
        return super.delete(id, TABLE_NAME);
    }

    @Override
    public boolean delete(Client client) {
        return false;
    }

    @Override
    public List<Client> getAll() {
        return super.getAll(TABLE_NAME).stream().map(s -> (Client) s).collect(Collectors.toList());
    }

    @Override
    public int size() {
        return super.size(TABLE_NAME);
    }
}
