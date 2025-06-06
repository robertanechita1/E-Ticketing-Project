package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GenericWriteService {
    // Singleton – instanta unica
    private static GenericWriteService instance;

    // singleton pattern
    private GenericWriteService() {

    }

    // met statica pt obtinerea instantei
    public static synchronized GenericWriteService getInstance() {
        if (instance == null) {
            instance = new GenericWriteService();
        }
        return instance;
    }

    public int create(String sql, Object... params) throws SQLException {
        try (Connection connection = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            return stmt.executeUpdate();
        }
    }

    public void update(String sql, Object... params) throws SQLException {
        try (Connection connection = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }

    public void delete(String sql, Object... params) throws SQLException {
        try (Connection connection = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            stmt.executeUpdate();
        }
    }
}
