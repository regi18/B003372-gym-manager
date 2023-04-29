package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, PK_TYPE> {

    T get(PK_TYPE pk);

    List<T> getAll();

    void insert(T t);

    void update(T t);

    void delete(T t);
}