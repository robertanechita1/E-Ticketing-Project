package services;

import classes.Artist;
import classes.Event;
import db.DatabaseContext;
import db.GenericReadService;
import db.GenericWriteService;
import helpers.ResultSetMapper;
import services.Audit.AuditService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArtistService {

    private final AuditService auditService = AuditService.getInstance();
    private final GenericReadService readSvc;
    private final GenericWriteService writeSvc;

    private static ArtistService instance;
    public ArtistService() throws SQLException {
        this.readSvc = GenericReadService.getInstance();
        this.writeSvc = GenericWriteService.getInstance();
    }
    public static synchronized ArtistService getInstance() throws SQLException {
        if (instance == null) {
            instance = new ArtistService();
        }
        return instance;
    }

    public void create(Artist artist) throws SQLException {
        String sql = "INSERT INTO artists (nume, descriere, views) VALUES (?, ?, ?)";
        writeSvc.create(sql,
                artist.getNume(),
                artist.getDescriere(),
                artist.getViews()
        );
        auditService.audit("CREATE", "Artist");
    }

    public Optional<Artist> readByNume(String nume) throws SQLException {
        String sql = "SELECT * FROM artists WHERE nume = ?";
        ResultSetMapper<Artist> mapper = (ResultSet rs) -> new Artist(
                rs.getString("nume"),
                rs.getString("descriere"),
                rs.getDouble("views")
        );
        Optional<Artist> maybe = readSvc.readOne(sql, mapper, nume);
        maybe.ifPresent(a -> {
            auditService.audit("READ", "Artist");
        });
        return maybe;
    }

    public List<Artist> readAll() throws SQLException {
        String sql = "SELECT * FROM artists";
        ResultSetMapper<Artist> mapper = (ResultSet rs) -> new Artist(
                rs.getString("nume"),
                rs.getString("descriere"),
                rs.getDouble("views")
        );
        List<Artist> lista = readSvc.readAll(sql, mapper);
        auditService.audit("READ_ALL", "Artist");
        return lista;
    }

    public void update(Artist artist) throws SQLException {
        String sql = "UPDATE artists SET descriere = ?, views = ? WHERE nume = ?";
        writeSvc.update(sql,
                artist.getDescriere(),
                artist.getViews(),
                artist.getNume()
        );
        auditService.audit("UPDATE", "Artist");
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
