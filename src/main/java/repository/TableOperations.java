package repository;

import java.sql.SQLException;
import java.util.List;

public interface TableOperations<T> {
    void clear();

    T update(T t);

    T save(T t);

    T get(Long id);

    boolean delete(Long id);

    boolean delete(T t);

    List<T> getAll();

    int size();
}
