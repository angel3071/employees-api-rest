package com.angel.dao;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * @param <T> The POJO class to perform actions on
 */
public interface Dao<T> {

    /**
     * @param id The Id to retreive the element
     * @return
     */
    Optional<T> get(String id);

    /**
     * @return An arraylist of the elements found on db
     * @throws IOException
     */
    List<T> getAll() throws IOException;

    /**
     * @param t The actual element to save to de db
     */
    void save(T t);

    /**
     * @param t The element to compleatly update on db
     */
    void update(T t);

    /**
     * @param id The key to perform the delete action
     */
    void delete(String id);
}
