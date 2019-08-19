package servlets.endpoints;

import com.google.gson.Gson;
import dbutils.SqlHelper;
import entities.Client;
import entities.dto.ClientDoc;
import entities.dto.ErrorDto;
import lombok.extern.log4j.Log4j;
import repository.ClientRepo;
import repository.ClientRepoInterface;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Log4j
@WebServlet("/clientgot")
public class ClientServlet extends HttpServlet {
    private SqlHelper sqlHelper;
    private ClientRepoInterface clientRepo;
    private static Gson gson = new Gson();

    public ClientServlet(SqlHelper sqlHelper) {
        this.sqlHelper = sqlHelper;
        clientRepo = new ClientRepo(sqlHelper);
    }

    public ClientServlet() {
        this(new SqlHelper("dev"));
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Servlet clientGot");
        request.setAttribute("error", null);
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
                if (!byDocument.isBlocked()) {
                    String client = gson.toJson(byDocument);
                    log.info("client = " + client);
                    out.print(client);
                } else {
                    ErrorDto err = new ErrorDto(508, "The user is blocked");
                    String error = gson.toJson(err);
                    request.setAttribute("error", error);
                    log.info("error = " + error);
                    out.print(request.getAttribute("error"));
                }
            } else {
                ErrorDto err = new ErrorDto(509, "The user is not found");
                String error = gson.toJson(err);
                log.info("error = " + error);
                out.print(error);
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
