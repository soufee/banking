package dbutils;

import exceptions.TableException;
import lombok.extern.log4j.Log4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Log4j
public class SqlHelper {
    // private String DB_URL = "jdbc:h2:/c:/temp/Revolut/banking/db/data";
    private static String DB_URL;
    private static final String DB_DRIVER = "org.h2.Driver";
    private static ConnectionFactory connectionFactory;

    static {
        DB_URL = "jdbc:h2:/" + System.getProperty("user.dir") + "/db/data.";
    }

    public SqlHelper(String env) {
        try {
            Class.forName(DB_DRIVER);
            connectionFactory = (() -> DriverManager.getConnection(DB_URL+env+";AUTO_SERVER=TRUE"));
        } catch (ClassNotFoundException e) {
            log.error(e);
        }
    }

    public <T> T execute(String sql, SqlExecutor<T> executor)  {
        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            return executor.executeAndGet(ps);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public <T> T transactionalExecute(SQLTransaction<T> executor) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = executor.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            throw new TableException(e);
        }
    }
}
