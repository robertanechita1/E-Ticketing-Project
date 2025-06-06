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

public class EventService {

    private final AuditService auditService = AuditService.getInstance();
    private final GenericReadService readSvc;
    private final GenericWriteService writeSvc;

    private static EventService instance;
    public EventService() throws SQLException {
        this.readSvc = GenericReadService.getInstance();
        this.writeSvc = GenericWriteService.getInstance();
    }
    public static synchronized EventService getInstance() throws SQLException {
        if (instance == null) {
            instance = new EventService();
        }
        return instance;
    }

    public void create(Event event) throws SQLException {
        String sql = "INSERT INTO events (nume, data, descriere, locatie, numar_bilete_disponibile, capacitate_totala, organizator, price) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        writeSvc.create(sql,
                event.getNume(),
                Date.valueOf(event.getData()),
                event.getDescriere(),
                event.getLocatie(),
                event.getNumarBileteDisponibile(),
                event.getCapacitateTotala(),
                event.getOrganizator(),
                event.getPrice()
        );
        auditService.audit("CREATE", "Event");
    }

    public Optional<Event> read(String nume) throws SQLException {
        String sql = "SELECT * FROM events WHERE nume = ?";
        ResultSetMapper<Event> mapper = (ResultSet rs) -> {
            Event ev = new Event(
                    rs.getString("nume"),
                    rs.getDate("data").toLocalDate(),
                    rs.getString("descriere"),
                    rs.getString("locatie"),
                    rs.getInt("numar_bilete_disponibile"),
                    rs.getInt("capacitate_totala"),
                    rs.getString("organizator"),
                    rs.getDouble("price")
            );
            return ev;
        };

        Optional<Event> maybe = readSvc.readOne(sql, mapper, nume);
        maybe.ifPresent(e -> {
            auditService.audit("READ", "Event");
        });
        return maybe;
    }

    public List<Event> readAll() throws SQLException {
        String sql = "SELECT * FROM events";
        ResultSetMapper<Event> mapper = (ResultSet rs) -> new Event(
                rs.getString("nume"),
                rs.getDate("data").toLocalDate(),
                rs.getString("descriere"),
                rs.getString("locatie"),
                rs.getInt("numar_bilete_disponibile"),
                rs.getInt("capacitate_totala"),
                rs.getString("organizator"),
                rs.getDouble("price")
        );
        List<Event> lista = readSvc.readAll(sql, mapper);
        auditService.audit("READ_ALL", "Event");
        return lista;
    }

    public void update(Event event) throws SQLException {
        String sql = "UPDATE events SET data = ?, descriere = ?, locatie = ?, numar_bilete_disponibile = ?, capacitate_totala = ?, organizator = ?, price = ? WHERE nume = ?";
        writeSvc.update(sql,
                Date.valueOf(event.getData()),
                event.getDescriere(),
                event.getLocatie(),
                event.getNumarBileteDisponibile(),
                event.getCapacitateTotala(),
                event.getOrganizator(),
                event.getPrice(),
                event.getNume()
        );
        auditService.audit("UPDATE", "Event");
    }

    public void delete(String nume) throws SQLException {
        String sqlBilete = "DELETE FROM bilete WHERE event_name = ?";
        String sqlEventArtists = "DELETE FROM event_artists WHERE event_nume = ?";
        String sqlEvent = "DELETE FROM events WHERE nume = ?";

        try (Connection conn = DatabaseContext.getWriteContext().getConnection();
             PreparedStatement stmtBilete = conn.prepareStatement(sqlBilete);
             PreparedStatement stmtEventArtists = conn.prepareStatement(sqlEventArtists);
             PreparedStatement stmtEvent = conn.prepareStatement(sqlEvent)) {

            // sterg biletele de la acest event
            stmtBilete.setString(1, nume);
            stmtBilete.executeUpdate();
            auditService.audit("DELETE", "bilete pentru eveniment: " + nume);

            // sterg legaturi artist-eveniment
            stmtEventArtists.setString(1, nume);
            stmtEventArtists.executeUpdate();
            auditService.audit("DELETE", "event_artists pentru eveniment: " + nume);

            // sterg eveniment
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
