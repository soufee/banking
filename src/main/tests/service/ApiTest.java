package service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import entities.Account;
import entities.Client;
import entities.Operation;
import entities.dto.PaymentResponseDto;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import repository.BaseTableTest;
import servlets.endpoints.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ApiTest extends BaseTableTest {
    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;
    @Mock
    BufferedReader reader;

    private AccountServlet accServlet = new AccountServlet(helper);
    private ClientServlet clientServlet = new ClientServlet(helper);
    private OperationServlet operServlet = new OperationServlet(helper);
    private PaymentServlet paymentServlet = new PaymentServlet(helper);
    private Gson gson = new Gson();
    private static Operation oper;
    private static Account acc1;
    private static Account acc2;

    @BeforeClass
    public static void setUp() {
        client1 = clientRepo.save(client1);
        client2 = clientRepo.save(client2);
        acc1 = Account.builder()
                .accountNumber("656")
                .amount(BigDecimal.valueOf(1000))
                .currencyCode("RUB")
                .owner(client1.getId())
                .isBlocked(false)
                .build();
        acc1 = accountRepo.save(acc1);
        acc2 = Account.builder()
                .accountNumber("657")
                .amount(BigDecimal.valueOf(1000))
                .currencyCode("RUB")
                .owner(client2.getId())
                .isBlocked(false)
                .build();
        acc2 = accountRepo.save(acc2);
        Operation operation = Operation.builder()
                .dateTime(LocalDateTime.now())
                .currency("RUB")
                .amount(BigDecimal.TEN)
                .from(acc1.getAccountNumber())
                .to(acc2.getAccountNumber())
                .build();
        oper = operationRepo.save(operation);
    }

    @Test
    public void getAccountTest() throws IOException, ServletException {
        when(reader.readLine()).thenReturn("{\"docNumber\": \"1616161616\"}").thenReturn(null);
        when(request.getReader()).thenReturn(reader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        accServlet.doPost(request, response);
        writer.flush();
        Type itemsListType = new TypeToken<List<Account>>() {
        }.getType();
        List<Account> accounts = gson.fromJson(stringWriter.toString(), itemsListType);
        Account account = accounts.stream().filter(s -> s.getAccountNumber().equals("656")).findFirst().orElse(null);
        assertNotNull(account);
    }

    @Test
    public void clientGotTest() throws IOException, ServletException {
        when(reader.readLine()).thenReturn("{\"docNumber\": \"1616161616\"}").thenReturn(null);
        when(request.getReader()).thenReturn(reader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        clientServlet.doPost(request, response);
        writer.flush();
        Client client = gson.fromJson(stringWriter.toString(), Client.class);
        assertNotNull(client);
    }

    @Test
    public void getOperationsTest() throws IOException, ServletException {
        when(reader.readLine()).thenReturn("{\"operId\": \"" + acc1.getAccountNumber() + "\"}").thenReturn(null);
        when(request.getReader()).thenReturn(reader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        operServlet.doPost(request, response);
        writer.flush();
        Type itemsListType = new TypeToken<List<Operation>>() {
        }.getType();
        List<Operation> operations = gson.fromJson(stringWriter.toString(), itemsListType);
        Operation operation = operations.stream().filter(s -> s.getId().equals(oper.getId())).findFirst().orElse(null);
        assertNotNull(operation);
    }

    @Test
    public void paymentTest() throws IOException, ServletException {
        String json = "{\"accountFrom\":\"656\",\"accountTo\":\"657\", \"amount\":\"15.50\"}";
        when(reader.readLine()).thenReturn(json).thenReturn(null);
        when(request.getReader()).thenReturn(reader);
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        paymentServlet.doPost(request, response);
        writer.flush();
        PaymentResponseDto resp = gson.fromJson(stringWriter.toString(), PaymentResponseDto.class);
        assertEquals(210, (int) resp.getStatus());

    }

}
