package servlets.endpoints;

import com.google.gson.Gson;
import dbutils.SqlHelper;
import entities.Account;
import entities.Client;
import entities.dto.ClientDoc;
import entities.dto.ErrorDto;
import lombok.extern.log4j.Log4j;
import repository.AccountRepo;
import repository.AccountRepoInterface;
import repository.ClientRepo;
import repository.ClientRepoInterface;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Log4j
@WebServlet("/getaccounts")
public class AccountServlet extends HttpServlet {
    private SqlHelper sqlHelper;
    private ClientRepoInterface clientRepo;
    private AccountRepoInterface accountRepo;
    private static Gson gson = new Gson();

    public AccountServlet() {
        this(new SqlHelper("dev"));
    }

    public AccountServlet(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
        clientRepo = new ClientRepo(sqlHelper);
        accountRepo = new AccountRepo(sqlHelper);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Get account servlet...");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        String json = ServletUtil.getJsonFromRequest(request);
        try {
            log.info(json);
            ClientDoc clientDoc = gson.fromJson(json, ClientDoc.class);
            log.info("clientDoc = " + clientDoc.getDocNumber());
            Client byDocument = clientRepo.getByDocument(clientDoc.getDocNumber());
            if (byDocument != null) {
                List<Account> accountsByClient = accountRepo.getAccountsByClient(byDocument);
                String accounts = gson.toJson(accountsByClient);
                out.print(accounts);
            }
        } catch (Exception e) {
            ErrorDto err = new ErrorDto(510, "Unexcpected exception. " + e.getMessage());
            String error = gson.toJson(err);
            log.info("error = " + error);
            out.print(error);
        } finally {
            out.flush();
        }


    }
}
