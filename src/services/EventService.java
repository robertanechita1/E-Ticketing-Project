package services;

import classes.Artist;
import classes.Event;
import db.DatabaseContext;
import services.Audit.AuditService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventService {

    public AuditService auditService = AuditService.getInstance();

    public void create(Event event) throws SQLException {
        String sql = "INSERT INTO events (nume, data, descriere, locatie, numar_bilete_disponibile, capacitate_totala, organizator, price) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, event.getNume());
            stmt.setDate(2, Date.valueOf(event.getData()));
            stmt.setString(3, event.getDescriere());
            stmt.setString(4, event.getLocatie());
            stmt.setInt(5, event.getNumarBileteDisponibile());
            stmt.setInt(6, event.getCapacitateTotala());
            stmt.setString(7, event.getOrganizator());
            stmt.setDouble(8, event.getPrice());
        
            stmt.executeUpdate();
            auditService.audit("CREATE", "Event");
        }
    }

    public Optional<Event> read(String nume) throws SQLException {
        String sql = "SELECT * FROM events WHERE nume = ?";

        try (Connection conn = DatabaseContext.getReadContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nume);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Event event = new Event(
                    rs.getString("nume"),
                    rs.getDate("data").toLocalDate(),
                    rs.getString("descriere"),
                    rs.getString("locatie"),
                    rs.getInt("capacitate_totala"),
                    rs.getString("organizator"),
                    rs.getDouble("price")
                );
                event.setNumarBileteDisponibile(rs.getInt("numar_bilete_disponibile"));

                auditService.audit("READ", "Event");

                return Optional.of(event);
            }
        }
        return Optional.empty();
    }

    public List<Event> readAll() throws SQLException {
        String sql = "SELECT * FROM events";
        List<Event> events = new ArrayList<>();

        try (Connection conn = DatabaseContext.getReadContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Event event = new Event(
                        rs.getString("nume"),
                        rs.getDate("data").toLocalDate(),
                        rs.getString("descriere"),
                        rs.getString("locatie"),
                        rs.getInt("capacitate_totala"),
                        rs.getString("organizator"),
                        rs.getDouble("price")
                );
                event.setNumarBileteDisponibile(rs.getInt("numar_bilete_disponibile"));
                events.add(event);
            }

            auditService.audit("READ_ALL", "Event");
        }

        return events;
    }

    public void update(Event event) throws SQLException {
        String sql = "UPDATE events SET data = ?, descriere = ?, locatie = ?, numar_bilete_disponibile = ?, capacitate_totala = ?, organizator = ?, price = ? WHERE nume = ?";
        
        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(event.getData()));
            stmt.setString(2, event.getDescriere());
            stmt.setString(3, event.getLocatie());
            stmt.setInt(4, event.getNumarBileteDisponibile());
            stmt.setInt(5, event.getCapacitateTotala());
            stmt.setString(6, event.getOrganizator());
            stmt.setDouble(7, event.getPrice());
            stmt.setString(8, event.getNume());

            auditService.audit("UPDATE", "Event");
            stmt.executeUpdate();
        }
    }

    public void delete(String nume) throws SQLException {
        String sqlEventArtists = "DELETE FROM event_artists WHERE event_nume = ?";
        String sqlEvent = "DELETE FROM events WHERE nume = ?";

        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmtEventArtists = conn.prepareStatement(sqlEventArtists);
             PreparedStatement stmtEvent = conn.prepareStatement(sqlEvent)) {

            stmtEventArtists.setString(1, nume);
            stmtEventArtists.executeUpdate();
            auditService.audit("DELETE", "event_artists pentru eveniment: " + nume);

            stmtEvent.setString(1, nume);
            stmtEvent.executeUpdate();
            auditService.audit("DELETE", "Event");
        }
    }


    public List<Artist> getArtistiPentruEveniment(String numeEveniment) throws SQLException {
        List<Artist> artisti = new ArrayList<>();

        String sql = "SELECT a.nume, a.descriere, a.views " +
                "FROM event_artists ea " +
                "JOIN artists a ON ea.artist_nume = a.nume " +
                "WHERE ea.event_nume = ?";

        try (Connection conn = DatabaseContext.getReadContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numeEveniment);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Artist artist = new Artist(
                        rs.getString("nume"),
                        rs.getString("descriere"),
                        rs.getDouble("views")
                );
                artisti.add(artist);
            }

            auditService.audit("READ", "event_artists pentru eveniment: " + numeEveniment);
        }

        return artisti;
    }

}
