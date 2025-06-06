package services;

import classes.User;
import db.DatabaseContext;
import db.GenericReadService;
import db.GenericWriteService;
import helpers.ResultSetMapper;
import services.Audit.AuditService;

import java.sql.*;
import java.util.Optional;

public class UserService {

    private final AuditService auditService = AuditService.getInstance();
    private final GenericReadService readSvc;
    private final GenericWriteService writeSvc;

    private static UserService instance;
    public UserService() throws SQLException {
        this.readSvc = GenericReadService.getInstance();
        this.writeSvc = GenericWriteService.getInstance();
    }
    public static synchronized UserService getInstance() throws SQLException {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public void create(User user) throws SQLException {
        String sql = "INSERT INTO users (nume, varsta, pass, role) VALUES (?, ?, ?, ?)";
        writeSvc.create(sql,
                user.getNume(),
                user.getVarsta(),
                user.getPass(),
                user.getRole()
        );
        auditService.audit("CREATE", "User");
    }

    public Optional<User> read(String nume) throws SQLException {
        String sql = "SELECT * FROM users WHERE nume = ?";

        ResultSetMapper<User> mapper = (ResultSet rs) -> new User(
                rs.getString("nume"),
                rs.getInt("varsta"),
                rs.getString("pass"),
                rs.getString("role")
        );

        Optional<User> maybe = readSvc.readOne(sql, mapper, nume);
        maybe.ifPresent(u -> {
            auditService.audit("READ", "User");
        });
        return maybe;
    }

    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET varsta = ?, pass = ?, role = ? WHERE nume = ?";
        writeSvc.update(sql,
                user.getVarsta(),
                user.getPass(),
                user.getRole(),
                user.getNume()
        );
        auditService.audit("UPDATE", "User");
    }

    public void delete(String nume) throws SQLException {
        String deleteBileteSql = "DELETE FROM bilete WHERE cumparator = ?";
        String deleteUserSql = "DELETE FROM users WHERE nume = ?";

        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(deleteBileteSql);
             PreparedStatement stmt2 = conn.prepareStatement(deleteUserSql)) {

            stmt1.setString(1, nume);
            stmt1.executeUpdate();
            auditService.audit("DELETE", "Bilet");

            stmt2.setString(1, nume);
            stmt2.executeUpdate();
            auditService.audit("DELETE", "User");
        }
    }
}
