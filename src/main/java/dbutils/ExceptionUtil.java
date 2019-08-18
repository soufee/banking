package dbutils;

import exceptions.TableException;

import java.sql.SQLException;

public class ExceptionUtil {
    private ExceptionUtil() {
    }

    public static TableException convertException(SQLException e) {
        return new TableException(e);
    }
}
