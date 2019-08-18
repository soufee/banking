package repository;

import entities.Client;

public interface ClientRepoInterface extends TableOperations<Client> {
    Client getByDocument(String docNumber);
}
