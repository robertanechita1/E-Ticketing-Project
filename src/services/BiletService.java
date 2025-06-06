package services;

import classes.Bilet;
import db.DatabaseContext;
import db.GenericReadService;
import db.GenericWriteService;
import helpers.ResultSetMapper;
import services.Audit.AuditService;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BiletService {

    private final AuditService auditService = AuditService.getInstance();
    private final GenericReadService readSvc;
    private final GenericWriteService writeSvc;

    private static BiletService instance;
    public BiletService() throws SQLException {
        this.readSvc = GenericReadService.getInstance();
        this.writeSvc = GenericWriteService.getInstance();
    }
    public static synchronized BiletService getInstance() throws SQLException {
        if (instance == null) {
            instance = new BiletService();
        }
        return instance;
    }

    public void create(Bilet bilet) throws SQLException {
        String sql = "INSERT INTO bilete (cod_unic, event_name, cumparator, valid, tip, plata) VALUES (?, ?, ?, ?, ?, ?)";
        writeSvc.create(sql,
                bilet.getCodUnic(),
                bilet.getEventName(),
                bilet.getCumparator(),
                bilet.esteValid(),
                bilet.getTip(),
                bilet.getPlata()
        );
        auditService.audit("CREATE", "Bilet");
    }

    public Optional<Bilet> read(String codUnic) throws SQLException {
        String sql = "SELECT * FROM bilete WHERE cod_unic = ?";
        ResultSetMapper<Bilet> mapper = (ResultSet rs) -> {
            Bilet b = new Bilet(
                    rs.getString("cod_unic"),
                    rs.getString("event_name"),
                    rs.getString("cumparator"),
                    rs.getBoolean("valid"),
                    rs.getString("tip"),
                    rs.getString("plata")
            );
            return b;
        };

        Optional<Bilet> maybe = readSvc.readOne(sql, mapper, codUnic);
        maybe.ifPresent(b -> {
            auditService.audit("READ", "Bilet");
        });
        return maybe;
    }

    public Optional<Bilet> readByCumparatorSiEvent(String cumparator, String event) throws SQLException {
        String sql = "SELECT * FROM bilete WHERE cumparator = ? AND event_name = ?";
        ResultSetMapper<Bilet> mapper = (ResultSet rs) -> {
            Bilet b = new Bilet(
                    rs.getString("cod_unic"),
                    rs.getString("event_name"),
                    rs.getString("cumparator"),
                    rs.getBoolean("valid"),
                    rs.getString("tip"),
                    rs.getString("plata")
            );
            return b;
        };

        Optional<Bilet> maybe = readSvc.readOne(sql, mapper, cumparator, event);
        maybe.ifPresent(b -> {
            auditService.audit("READ", "Bilet");
        });
        return maybe;
    }

    public List<Bilet> readAll() throws SQLException {
        String sql = "SELECT * FROM bilete";
        ResultSetMapper<Bilet> mapper = (ResultSet rs) -> new Bilet(
                rs.getString("cod_unic"),
                rs.getString("event_name"),
                rs.getString("cumparator"),
                rs.getBoolean("valid"),
                rs.getString("tip"),
                rs.getString("plata")
        );
        List<Bilet> lista = readSvc.readAll(sql, mapper);
        auditService.audit("READ_ALL", "Bilet");
        return lista;
    }

    public void update(Bilet bilet) throws SQLException {
        String sql = "UPDATE bilete SET event_name = ?, cumparator = ?, valid = ?, tip = ?, plata = ? WHERE cod_unic = ?";
        writeSvc.update(sql,
                bilet.getEventName(),
                bilet.getCumparator(),
                bilet.esteValid(),
                bilet.getTip(),
                bilet.getPlata(),
                bilet.getCodUnic()
        );
        auditService.audit("UPDATE", "Bilet");
    }

    public void delete(String codUnic) throws SQLException {
        String sql = "DELETE FROM bilete WHERE cod_unic = ?";
        writeSvc.delete(sql, codUnic);
        auditService.audit("DELETE", "Bilet");
    }
}
