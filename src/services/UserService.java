package services;

import classes.User;
import db.DatabaseContext;
import services.Audit.AuditService;

import java.sql.*;
import java.util.Optional;

public class UserService {

    public AuditService auditService = AuditService.getInstance();

    public void create(User user) throws SQLException {
        String sql = "INSERT INTO users (nume, varsta, pass, role) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getNume());
            stmt.setInt(2, user.getVarsta());
            stmt.setString(3, user.getPass());
            stmt.setString(4, user.getRole());
            
            stmt.executeUpdate();

            auditService.audit("CREATE", "User");
        }
    }

    public Optional<User> read(String nume) throws SQLException {
        String sql = "SELECT * FROM users WHERE nume = ?";

        try (Connection conn = DatabaseContext.getReadContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nume);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User(
                    rs.getString("nume"),
                    rs.getInt("varsta"),
                    rs.getString("pass"),
                    rs.getString("role"));
                
                auditService.audit("READ", "User");

                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET varsta = ?, pass = ?, role = ? WHERE nume = ?";

        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, user.getVarsta());
            stmt.setString(2, user.getPass());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getNume());

            stmt.executeUpdate();
            auditService.audit("UPDATE", "User");
        }
    }

    public void delete(String nume) throws SQLException {
        String sql = "DELETE FROM users WHERE nume = ?";

        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nume);
            
            stmt.executeUpdate();
            auditService.audit("DELETE", "User");
        }
    }
}
