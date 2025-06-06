package helpers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * interfata care mapeaza un ResultSet (aflat deja pe un row valid)
 * la un obiect de tipul <T>.
 */
@FunctionalInterface
public interface ResultSetMapper<T> {
    T map(ResultSet rs) throws SQLException;
}
