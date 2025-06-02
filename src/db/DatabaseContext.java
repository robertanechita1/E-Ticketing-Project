package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseContext {
    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "robi19";

    private static DatabaseContext READ_INSTANCE;
    private static DatabaseContext WRITE_INSTANCE;

    private Connection connection;

    private DatabaseContext() throws SQLException {
        this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public static synchronized DatabaseContext getReadContext() throws SQLException {
        if (READ_INSTANCE == null || READ_INSTANCE.connection.isClosed()) {
            READ_INSTANCE = new DatabaseContext();
        }
        return READ_INSTANCE;
    }

    public static synchronized DatabaseContext getWriteContext() throws SQLException {
        if (WRITE_INSTANCE == null || WRITE_INSTANCE.connection.isClosed()) {
            WRITE_INSTANCE = new DatabaseContext();
        }
        return WRITE_INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement()) {

            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    nume VARCHAR(100) UNIQUE NOT NULL,
                    varsta INT NOT NULL,
                    pass VARCHAR(100) NOT NULL,
                    role VARCHAR(50)
                );
                """;

            String createEventsTable = """
                    CREATE TABLE IF NOT EXISTS events (
                        id SERIAL PRIMARY KEY,
                        nume VARCHAR(100) UNIQUE NOT NULL,
                        data DATE,
                        descriere TEXT,
                        locatie VARCHAR(100),
                        numar_bilete_disponibile INT,
                        capacitate_totala INT,
                        organizator VARCHAR(100),
                        price DOUBLE PRECISION
                    );
                """;

            String createArtistTable = """
                    CREATE TABLE IF NOT EXISTS artists (
                        id SERIAL PRIMARY KEY,
                        nume VARCHAR(100) UNIQUE NOT NULL,
                        descriere TEXT,
                        views DOUBLE PRECISION
                    );
                    """;

            String createTicketsTable = """
                    CREATE TABLE IF NOT EXISTS bilete (
                        id SERIAL PRIMARY KEY,
                        cod_unic VARCHAR(50),
                        event_name VARCHAR(100) NOT NULL REFERENCES events(nume),
                        cumparator VARCHAR(100) REFERENCES users(nume),
                        valid BOOLEAN DEFAULT FALSE,
                        tip VARCHAR(50),
                        plata VARCHAR(50)
                    );
                    """;

            String createEventsArtitsTable = """
                    CREATE TABLE IF NOT EXISTS event_artists (
                        event_nume varchar(100) REFERENCES events(nume),
                        artist_nume VARCHAR(100) REFERENCES artists(nume),
                        data DATE,
                        PRIMARY KEY (event_nume, artist_nume)
                    );
                    """;

            stmt.executeUpdate(createUsersTable);
            stmt.executeUpdate(createEventsTable);
            stmt.executeUpdate(createArtistTable);
            stmt.executeUpdate(createTicketsTable);
            stmt.executeUpdate(createEventsArtitsTable);

            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
