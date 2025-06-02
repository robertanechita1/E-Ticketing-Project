package services;

import classes.Bilet;
import db.DatabaseContext;
import services.Audit.AuditService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BiletService {

    public AuditService auditService = AuditService.getInstance();

    public void create(Bilet bilet) throws SQLException {
        String sql = "INSERT INTO bilete (cod_unic, event_name, cumparator, valid, tip, plata) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bilet.getCodUnic());
            stmt.setString(2, bilet.getEventName());
            stmt.setString(3, bilet.getCumparator());
            stmt.setBoolean(4, bilet.esteValid());
            stmt.setString(5, bilet.getTip());
            stmt.setString(6, bilet.getPlata());
            
            stmt.executeUpdate();

            auditService.audit("CREATE", "Bilet");
        }
    }

    public Optional<Bilet> read(String codUnic) throws SQLException {
        String sql = "SELECT * FROM bilete WHERE cod_unic = ?";
        try (Connection conn = DatabaseContext.getReadContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codUnic);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Bilet bilet = new Bilet(
                    rs.getString("event_name"),
                    rs.getString("cumparator"),
                    rs.getString("tip"),
                    rs.getString("plata")
                );

                bilet.setCodUnic(rs.getString("cod_unic"));
                bilet.setValid(rs.getBoolean("valid"));
            
                auditService.audit("READ", "Bilet");

                return Optional.of(bilet);
            }
        }
        return Optional.empty();
    }

    public List<Bilet> readAll() throws SQLException {
        String sql = "SELECT * FROM bilete";
        List<Bilet> bilete = new ArrayList<>();

        try (Connection conn = DatabaseContext.getReadContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Bilet bilet = new Bilet(
                        rs.getString("event_name"),
                        rs.getString("cumparator"),
                        rs.getString("tip"),
                        rs.getString("plata")
                );
                bilet.setCodUnic(rs.getString("cod_unic"));
                bilet.setValid(rs.getBoolean("valid"));

                bilete.add(bilet);
            }

            auditService.audit("READ_ALL", "Bilet");
        }

        return bilete;
    }

    public void update(Bilet bilet) throws SQLException {
        String sql = "UPDATE bilete SET event_name = ?, cumparator = ?, valid = ?, tip = ?, plata = ? WHERE cod_unic = ?";
        
        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, bilet.getEventName());
            stmt.setString(2, bilet.getCumparator());
            stmt.setBoolean(3, bilet.esteValid());
            stmt.setString(4, bilet.getTip());
            stmt.setString(5, bilet.getPlata());
            stmt.setString(6, bilet.getCodUnic());
            
            stmt.executeUpdate();

            auditService.audit("UPDATE", "Bilet");
        }
    }

    public void delete(String codUnic) throws SQLException {
        String sql = "DELETE FROM bilete WHERE cod_unic = ?";
        
        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codUnic);
            
            stmt.executeUpdate();

            auditService.audit("DELETE", "Bilet");
        }
    }
}
