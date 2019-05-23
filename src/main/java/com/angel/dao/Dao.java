package com.angel.dao;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(String id);
    List<T> getAll() throws IOException;
    void save(T t);
    void update(T t);
    void delete(String id);
}
