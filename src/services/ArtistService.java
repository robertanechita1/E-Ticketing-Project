package services;

import classes.Artist;
import classes.Event;
import db.DatabaseContext;
import services.Audit.AuditService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtistService {

    public AuditService auditService = AuditService.getInstance();

    public void create(Artist artist) throws SQLException {
        String sql = "INSERT INTO artists (nume, descriere, views) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artist.getNume());
            stmt.setString(2, artist.getDescriere());
            stmt.setDouble(3, artist.getViews());

            stmt.executeUpdate();
            auditService.audit("CREATE", "Artist");
        }
    }

    public Optional<Artist> read(String nume) throws SQLException {
        String sql = "SELECT * FROM artists WHERE nume = ?";

        try (Connection conn = DatabaseContext.getReadContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nume);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Artist artist = new Artist(
                        rs.getString("nume"),
                        rs.getString("descriere"),
                        rs.getDouble("views")
                );
                auditService.audit("READ", "Artist");
                return Optional.of(artist);
            }
        }

        return Optional.empty();
    }

    public void update(Artist artist) throws SQLException {
        String sql = "UPDATE artists SET descriere = ?, views = ? WHERE nume = ?";

        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, artist.getDescriere());
            stmt.setDouble(2, artist.getViews());
            stmt.setString(3, artist.getNume());

            stmt.executeUpdate();
            auditService.audit("UPDATE", "Artist");
        }
    }

    public void delete(String nume) throws SQLException {
        String deleteEventArtistsSql = "DELETE FROM event_artists WHERE artist_nume = ?";
        String deleteArtistSql = "DELETE FROM artists WHERE nume = ?";

        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt1 = conn.prepareStatement(deleteEventArtistsSql);
             PreparedStatement stmt2 = conn.prepareStatement(deleteArtistSql)) {

            // sterg din event_artists mai intai
            stmt1.setString(1, nume);
            stmt1.executeUpdate();
            auditService.audit("DELETE", "Event_Artist");

            // apoi din artists
            stmt2.setString(1, nume);
            stmt2.executeUpdate();
            auditService.audit("DELETE", "Artist");
        }
    }



    // artist - eveniment
    public Optional<Boolean> adaugaArtistLaEveniment(String numeArtist, String numeEveniment, Date dataConcertului) throws SQLException {
        String sql = "INSERT INTO event_artists (event_nume, artist_nume, data) " +
                "VALUES (?, ?, ?)";

        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numeEveniment);
            stmt.setString(2, numeArtist);
            stmt.setDate(3, dataConcertului);

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                // nu s-a inserat nimic (artist sau eveniment nu exista)
                return Optional.empty();
            } else {
                auditService.audit("CREATE", "event_artists");
                return Optional.of(true);
            }
        }
    }
}
