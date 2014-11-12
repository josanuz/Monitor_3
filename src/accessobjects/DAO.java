package accessobjects;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by Casa on 03/11/2014.
 */
public interface DAO<T, K> {
    public T getBy(K pk) throws SQLException;

    public boolean insert(T what) throws SQLException;

    public List<T> getAll() throws SQLException;

    public boolean update(T what) throws SQLException;

    public boolean delete(T what) throws SQLException;

    public List<T> select(Predicate<T> p) throws SQLException;

    public void deleteAll() throws SQLException;
}
