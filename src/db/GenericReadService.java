package db;

import helpers.ResultSetMapper;

import java.sql.*;
import java.util.Optional;

public class GenericReadService {
    private static GenericReadService instance;

    private GenericReadService() {
        // constructor gol — conexiunile vor fi create la fiecare apel
    }

    //serviciu singleton, o sg instanta
    public static synchronized GenericReadService getInstance() {
        if (instance == null) {
            instance = new GenericReadService();
        }
        return instance;
    }

    public <T> Optional<T> readOne(String sql, ResultSetMapper<T> mapper, Object... params) throws SQLException {
        try (Connection connection = DatabaseContext.getReadContext().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    T entity = mapper.map(rs);
                    return Optional.of(entity);
                }
            }
        }
        return Optional.empty();
    }

    public <T> java.util.List<T> readAll(String sql, ResultSetMapper<T> mapper, Object... params) throws SQLException {
        java.util.List<T> result = new java.util.ArrayList<>();
        try (Connection connection = DatabaseContext.getReadContext().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    T entity = mapper.map(rs);
                    result.add(entity);
                }
            }
        }
        return result;
    }
}
