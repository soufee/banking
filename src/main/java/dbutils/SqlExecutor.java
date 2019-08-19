package dbutils;

import java.sql.PreparedStatement;

public interface SqlExecutor<T> {
    T executeAndGet(PreparedStatement statement) throws Exception;
}
