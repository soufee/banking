package repository;

import dbutils.SqlHelper;
import entities.Account;
import entities.Client;

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
    public void clear() {
        super.clear(TABLE_NAME);
    }

    @Override
    public void update(Client client) {

    }

    @Override
    public void save(Client client) {

    }

    @Override
    public Client get(long id) {
        return null;
    }

    @Override
    public void delete(long id) {
        super.delete(id, TABLE_NAME);
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
