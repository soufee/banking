package repository;

import com.google.common.base.Preconditions;
import dbutils.SqlHelper;
import entities.Account;
import entities.Client;
import entities.DBEntity;
import lombok.extern.log4j.Log4j;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Log4j
public class ClientRepo extends BaseTable implements ClientRepoInterface {
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
    public Client update(Client client) {
        Preconditions.checkNotNull(client);
        log.debug("Updating client " + client.getId());
        Client toUpdate = get(client.getId());
        if (toUpdate != null) {
            return helper.execute("UPDATE " + TABLE_NAME + " SET " +
                    "firstname = ?, " +
                    "lastname = ?, " +
                    "isblocked = ?, " +
                    "address = ?, " +
                    "phone = ?, " +
                    "document = ?, " +
                    "sex = ? " +
                    "where id = ?", ps -> {
                ps.setString(1, client.getFirstName());
                ps.setString(2, client.getLastName());
                ps.setBoolean(3, client.isBlocked());
                ps.setString(4, client.getAddress());
                ps.setString(5, client.getPhone());
                ps.setString(6, client.getDocument());
                ps.setString(7, client.getSex());
                ps.setLong(8, client.getId());
                if (ps.executeUpdate() != 1) {
                    log.warn("Exception while updating the record " + client.getId() + " in " + TABLE_NAME);
                    throw new RuntimeException("Exception while updating the client record with id " + client.getId());
                }
                Client result = get(client.getId());
                log.debug("Updated client = " + result.getId() + ": " + result.getFirstName() + " " + result.getLastName());
                return result;
            });
        } else {
            log.debug("Could not update. The record " + client.getId() + " does not exist in table " + TABLE_NAME);
            return save(client);
        }
    }

    @Override
    public Client save(Client client) {
        Preconditions.checkNotNull(client);
        log.debug("Saving client " + client.getId());
        Client gotClientById = get(client.getId());
        if (gotClientById != null) {
            log.debug("The client " + client.getId() + " already exists...");
            return update(gotClientById);
        } else {
            return helper.transactionalExecute(conn -> {
                PreparedStatement statement = conn.prepareStatement(
                        "INSERT INTO " + TABLE_NAME + " (FIRSTNAME, LASTNAME, ADDRESS, PHONE, SEX, ISBLOCKED, DOCUMENT)" +
                                " VALUES (?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                statement.setString(1, client.getFirstName());
                statement.setString(2, client.getLastName());
                statement.setString(3, client.getAddress());
                statement.setString(4, client.getPhone());
                statement.setString(5, client.getSex());
                statement.setBoolean(6, client.isBlocked());
                statement.setString(7, client.getDocument());
                statement.execute();
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    long id = resultSet.getLong(1);
                    client.setId(id);
                    log.debug("Saved client with id "+id);
                }
                return client;
            });
        }
    }

    @Override
    public Client get(Long id) {
        log.debug("Get Client " + id);
        return (Client) super.get(id, TABLE_NAME);
    }

    private boolean delete(Long id) {
        return super.delete(id, TABLE_NAME);
    }

    @Override
    public boolean delete(Client client) {
        Preconditions.checkNotNull(client);
        log.debug("delete client " + client.getId());
        Client byDocument = getByDocument(client.getDocument());
        if (byDocument != null)
            return delete(byDocument.getId());
        else return false;
    }

    @Override
    public List<Client> getAll() {
        log.debug("getAll clients ");
        return super.getAll(TABLE_NAME).stream().map(s -> (Client) s).collect(Collectors.toList());
    }

    @Override
    public int size() {
        int size = super.size(TABLE_NAME);
        log.debug("Size of table " + TABLE_NAME + " = " + size);
        return size;
    }

    @Override
    public Client getByDocument(String docNumber) {
        Preconditions.checkState(docNumber.length() > 0 && docNumber.length() <= 10, "Document number must not be empty and must have length not more than 10 characters");
        log.debug("Getting Client by Document " + docNumber);
        return getAll().stream().filter(s -> s.getDocument().equals(docNumber)).findFirst().orElse(null);
    }


    @Override
    protected List<DBEntity> getEntityFromResultSet(ResultSet rs) throws Exception {
        log.debug("Geting list of Clients by ResultSet ");
        List<DBEntity> list = new ArrayList<>();
        while (rs.next()) {
            list.add(Client.builder()
                    .id(rs.getLong("id"))
                    .firstName(rs.getString("FIRSTNAME"))
                    .lastName(rs.getString("LASTNAME"))
                    .address(rs.getString("ADDRESS"))
                    .phone(rs.getString("PHONE"))
                    .sex(rs.getString("SEX"))
                    .isBlocked(rs.getBoolean("isblocked"))
                    .document(rs.getString("DOCUMENT"))
                    .build());
        }
        log.debug("Size of list got from ResultSet =  " + list.size());
        return list;
    }
}
