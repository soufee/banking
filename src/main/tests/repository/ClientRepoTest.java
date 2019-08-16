package repository;

import entities.Client;
import lombok.extern.log4j.Log4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

@Log4j
public class ClientRepoTest extends BaseTableTest {

    @Before
    public void setUp() {
        clientRepo.save(client1);
        clientRepo.save(client2);
        clientRepo.save(client3);
    }

    @After
    public void clearData() {
        clientRepo.clear();
    }

    @Test
    public void clear() {
        int size = clientRepo.size();
        assertNotEquals(0, size);
        clientRepo.clear();
        size = clientRepo.getAll().size();
        assertEquals(0, size);

    }

    @Test
    public void update() {
        Client clToUpdate = clientRepo.getByDocument("1616161616");
        String prevName = clToUpdate.getFirstName();
        assertFalse(clToUpdate.isBlocked());
        clToUpdate.setBlocked(true);
        clToUpdate.setFirstName("banned-" + clToUpdate.getFirstName());
        clientRepo.update(clToUpdate);
        Client updated = clientRepo.getByDocument("1616161616");
        assertTrue(updated.isBlocked());
        assertEquals(updated.getFirstName(), "banned-" + prevName);
    }

    @Test
    public void save() {
        Client newClient = Client.builder()
                .firstName("Ashamaz")
                .lastName("Shomakhov")
                .address("Russia, Moscow")
                .document("2929989898")
                .isBlocked(false)
                .sex("MALE")
                .phone("9898989898")
                .build();
        clientRepo.save(newClient);
        Client byDocument = clientRepo.getByDocument("2929989898");
        assertNotNull(byDocument);
    }

    @Test
    public void get() {
        Client clientFromDB = clientRepo.getByDocument(client1.getDocument());
        Client client = clientRepo.get(clientFromDB.getId());
        assertEquals(client1, client);
    }

    @Test
    public void delete() {
        List<Client> all = clientRepo.getAll();
        assertTrue(all.contains(client1));
        assertTrue(all.contains(client2));
        assertTrue(all.contains(client3));
        clientRepo.delete(client2);
        all = clientRepo.getAll();
        assertFalse(all.contains(client2));
        assertTrue(all.contains(client1));
        assertTrue(all.contains(client3));
    }

    @Test
    public void getAll() {
        List<Client> all = clientRepo.getAll();
        assertNotNull(all);
        assertTrue(all.size() != 0);
    }

    @Test
    public void size() {
        assertEquals(3, clientRepo.size());
    }

    @Test
    public void getByDocument() {
        Client byDocument = clientRepo.getByDocument("2000222111");
        assertNotNull(byDocument);
        assertEquals(byDocument.getFirstName(), "Miroslava");
        assertEquals(byDocument.getLastName(), "Karpovic");
    }

}