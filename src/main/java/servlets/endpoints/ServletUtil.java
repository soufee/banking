package servlets.endpoints;

import lombok.extern.log4j.Log4j;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

@Log4j
public class ServletUtil {
    public static String getJsonFromRequest(HttpServletRequest request) throws IOException {
        log.info("Entering getJsonFromRequest...");
        try (BufferedReader reader = request.getReader()) {
            StringBuilder sb = new StringBuilder();
            String line = null;
            request.setAttribute("error", null);
            while ((line = reader.readLine()) != null)
                sb.append(line);
            return sb.toString();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw e;
        }

    }
}
