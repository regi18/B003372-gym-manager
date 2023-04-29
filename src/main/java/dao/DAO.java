package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T, PK_TYPE> {

    T get(PK_TYPE pk);

    List<T> getAll();

    int insert(T t);

    int update(T t);

    int delete(T t);
}