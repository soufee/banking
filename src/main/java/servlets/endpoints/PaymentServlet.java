package servlets.endpoints;

import com.google.gson.Gson;
import dbutils.SqlHelper;
import entities.Account;
import entities.Operation;
import entities.dto.PaymentDto;
import exceptions.TransactionException;
import lombok.extern.log4j.Log4j;
import repository.AccountRepo;
import repository.AccountRepoInterface;
import repository.OperationRepo;
import repository.OperationRepoInterface;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.concurrent.*;

@Log4j
@WebServlet("/payment")
public class PaymentServlet extends HttpServlet {
    private SqlHelper sqlHelper = new SqlHelper("dev");
    private static Gson gson = new Gson();
    private OperationRepoInterface operRepo = new OperationRepo(sqlHelper);
    private AccountRepoInterface accRepo = new AccountRepo(sqlHelper);

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Зашли в сервлет PaymentServlet в метод doPost");
        String json = ServletUtil.getJsonFromRequest(request);
        log.info(json);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        PaymentDto paymentDt = gson.fromJson(json, PaymentDto.class);
        BigDecimal amount = BigDecimal.valueOf(Double.valueOf(paymentDt.getAmount()));
        Operation operation = Operation.builder().currency("RUB").amount(amount).dateTime(LocalDateTime.now()).from(paymentDt.getAccountFrom()).to(paymentDt.getAccountTo()).build();
        log.debug(operation);
        String result = null;
        try{
            result = doTransaction(operation);
        } catch (Exception e){
            log.error(e);
        }
        log.info(result!=null? result: "error");
        out.print(result);
        log.info(result);
    }


    private synchronized void rollBack(Operation operation, Account from, Account to) {
        try {
            if (operation.getId() != null) {
                operRepo.delete(operation);
            }
            if (from != null) {
                from.setAmount(from.getAmount().add(operation.getAmount()));
                accRepo.update(from);
            }
            if (to != null) {
                to.setAmount(to.getAmount().subtract(operation.getAmount()));
                accRepo.save(to);
            }
        } catch (Exception e) {
            log.error("Could not rollback operation " + operation);
            throw e;
        }

    }

    private synchronized String doTransaction(Operation operation) {
            BigDecimal amount = operation.getAmount();
            Account accountFrom = accRepo.getAccountByAccountNumber(operation.getFrom());
            if (accountFrom.getAmount().compareTo(amount) < 0) {
                throw new TransactionException("Not enough money on account " + accountFrom.getAccountNumber());
            }
            Account accountTo = accRepo.getAccountByAccountNumber(operation.getTo());
            accountFrom.setAmount(accountFrom.getAmount().subtract(amount));
            accountTo.setAmount(accountTo.getAmount().add(amount));
            saveOperationAndAccounts(operation, accountFrom, accountTo);
        return "operation saved succesfully " + operation;
    }

    private synchronized void saveOperationAndAccounts(Operation operation, Account accountFrom, Account accountTo) {
        Account accountFrom2 = null;
        Account accountTo2 = null;
        try {
            operation = operRepo.save(operation);
            accountFrom2 = accRepo.update(accountFrom);
            accountTo2 = accRepo.update(accountTo);
        } catch (Exception e) {
            rollBack(operation, accountFrom2, accountTo2);
            throw e;
        }
    }

}
