package servlets.endpoints;

import com.google.gson.Gson;
import dbutils.SqlHelper;
import entities.Account;
import entities.Operation;
import entities.dto.ErrorDto;
import entities.dto.OperationDto;
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
import java.util.ArrayList;
import java.util.List;

@Log4j
@WebServlet("/getoperations")
public class OperationServlet extends HttpServlet {
    private SqlHelper sqlHelper;
    private OperationRepoInterface operRepo;
    private AccountRepoInterface accRepo;
    private static Gson gson = new Gson();

    public OperationServlet(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
        operRepo = new OperationRepo(sqlHelper);
        accRepo = new AccountRepo(sqlHelper);
    }

    public OperationServlet() {
        this(new SqlHelper("dev"));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Get operation servlet...");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String json = ServletUtil.getJsonFromRequest(request);
        try {
            OperationDto operationDto = gson.fromJson(json, OperationDto.class);
            Account account = accRepo.getAccountByAccountNumber(operationDto.getOperId());
            List<Operation> allOperationsOfAccount = getAllOperationsOfAccount(account);
            String operations = gson.toJson(allOperationsOfAccount);
            log.info(operations);
            out.print(operations);
        } catch (Exception e) {
            log.error(e);
            ErrorDto err = new ErrorDto(510, "Unexcpected exception. " + e.getMessage());
            String error = gson.toJson(err);
            log.info("error = " + error);
            out.print(error);
        } finally {
            out.flush();
        }
    }

    private List<Operation> getAllOperationsOfAccount(Account account) {
        List<Operation> operations = new ArrayList<>();
        operations.addAll(operRepo.getAllIncomeOperations(account));
        operations.addAll(operRepo.getAllOutcomeOperations(account));
        return operations;
    }


}
