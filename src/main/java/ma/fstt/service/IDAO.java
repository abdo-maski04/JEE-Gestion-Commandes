package ma.fstt.service;

import java.sql.SQLException;
import java.util.List;

public interface IDAO <T,ID>{

    void save(T entity) throws SQLException;

    List<T> findAll() throws SQLException;

    T findById(ID id) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(T entity) throws SQLException;
}
