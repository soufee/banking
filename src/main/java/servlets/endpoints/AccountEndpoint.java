package servlets.endpoints;

import coders.*;
import dbutils.SqlHelper;
import entities.Account;
import entities.Client;
import entities.Message;
import entities.Operation;
import exceptions.TransactionException;
import lombok.extern.log4j.Log4j;
import repository.*;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.*;

@Log4j
@ServerEndpoint(value = "/client/{client}", decoders = {OperationDecoder.class, MessageDecoder.class, ClientDecoder.class, AccountDecoder.class}, encoders = {OperationEncoder.class, AccountEncoder.class, MessageEncoder.class, ClientEncoder.class})
public class AccountEndpoint {
    private Session session = null;
    private Client client;
    private static List<Session> sessionList = new LinkedList<>();
    private SqlHelper sqlHelper = new SqlHelper("dev");
    private ClientRepoInterface clientRepo = new ClientRepo(sqlHelper);
    private AccountRepoInterface accountRepo = new AccountRepo(sqlHelper);
    private OperationRepoInterface operationRepo = new OperationRepo(sqlHelper);

    @OnOpen
    public void onOpen(Session session, @PathParam("client") String document) throws IOException, EncodeException {
        log.info("Opening session for client by document " + document);
        this.session = session;
        this.client = clientRepo.getByDocument(document);
        sessionList.add(session);
        log.info("session opened for client " + client);
    }

    @OnClose
    public void onClose(Session session) {
        log.info("Closing session for client " + client);
        sessionList.remove(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Error occured", throwable);
    }

    @OnMessage
    public void onMessage(Session session, Client client) {
        try {
            log.info("seinding data of client " + this.client.getFirstName() + " " + this.client.getLastName());
            List<Account> accountsByClient = accountRepo.getAccountsByClient(this.client);
            Set<Operation> operations = new HashSet<>();
            for (Account acc : accountsByClient) {
                operations.addAll(getAllOperationsOfAccount(acc));
            }
            Message message = new Message(this.client, accountsByClient, operations);
            session.getBasicRemote().sendObject(message);
        } catch (Exception e) {
            log.error("Could not operate...", e);
        }
    }

    private List<Operation> getAllOperationsOfAccount(Account account) {
        List<Operation> operations = new ArrayList<>();
        operations.addAll(operationRepo.getAllIncomeOperations(account));
        operations.addAll(operationRepo.getAllOutcomeOperations(account));
        return operations;
    }

    private synchronized void rollBack(Operation operation, Account from, Account to) {
        try {
            if (operation.getId() != null) {
                operationRepo.delete(operation);
            }
            if (from != null) {
                from.setAmount(from.getAmount().add(operation.getAmount()));
                accountRepo.update(from);
            }
            if (to != null) {
                to.setAmount(to.getAmount().subtract(operation.getAmount()));
                accountRepo.save(to);
            }
        } catch (Exception e) {
            log.error("Could not rollback operation " + operation);
        }

    }

    private synchronized String doTransaction(Operation operation) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future future = executor.submit(() -> {
            BigDecimal amount = operation.getAmount();
            Account accountFrom = accountRepo.getAccountByAccountNumber(operation.getFrom());
            if (accountFrom.getAmount().compareTo(amount) < 0) {
                throw new TransactionException("Not enough money on account " + accountFrom.getAccountNumber());
            }
            Account accountTo = accountRepo.getAccountByAccountNumber(operation.getTo());
            accountFrom.setAmount(accountFrom.getAmount().subtract(amount));
            accountTo.setAmount(accountTo.getAmount().add(amount));
            saveOperationAndAccounts(operation, accountFrom, accountTo);
        });

        try {
            future.get(1, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            log.error("Error occured while transacion provided...", e);
        }
        return "operation saved succesfully " + operation;
    }

    private synchronized void saveOperationAndAccounts(Operation operation, Account accountFrom, Account accountTo) {
        Account accountFrom2 = null;
        Account accountTo2 = null;
        try {
            operation = operationRepo.save(operation);
            accountFrom2 = accountRepo.update(accountFrom);
            accountTo2 = accountRepo.update(accountTo);
        } catch (Exception e) {
            rollBack(operation, accountFrom2, accountTo2);
        }
    }

}
