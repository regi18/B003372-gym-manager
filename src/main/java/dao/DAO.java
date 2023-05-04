package dao;

import java.util.List;

public interface DAO<T, ID> {

    T get(ID id);

    List<T> getAll();

    void insert(T t);

    void update(T t);

    boolean delete(ID id);
}