package repository;

import java.sql.SQLException;
import java.util.List;

public interface TableOperations<T> {
    void clear();

    void update(T t);

    void save(T t);

    T get(long id);

    void delete(long id);

    List<T> getAll();

    int size();
}
