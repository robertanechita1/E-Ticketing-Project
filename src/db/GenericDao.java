package db;

import classes.Artist;
import db.DbConnection;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class GenericDao<T> {
    private static final Map<Class<?>, GenericDao<?>> instances = new HashMap<>();
    private final Class<T> type;

    private GenericDao(Class<T> type) {
        this.type = type;
    }

    public static synchronized <T> GenericDao<T> getInstance(Class<T> type) {
        if (!instances.containsKey(type)) {
            instances.put(type, new GenericDao<>(type));
        }
        return (GenericDao<T>) instances.get(type);
    }

    private List<Field> getPersistentFields(Field[] fields) {
        List<Field> result = new ArrayList<>();
        for (Field field : fields) {
            if (!List.class.isAssignableFrom(field.getType())) {
                result.add(field);
            }
        }
        return result;
    }


    //create
    public void addObj(T entity) throws Exception {
        Field[] allFields = type.getDeclaredFields();
        List<Field> fields = getPersistentFields(allFields);

        StringBuilder sql = new StringBuilder("INSERT INTO ");
        sql.append(type.getSimpleName().toLowerCase()).append("s (");

        for (Field field : fields) {
            sql.append(field.getName()).append(",");
        }
        sql.setLength(sql.length() - 1); // remove trailing comma
        sql.append(") VALUES (").append("?,".repeat(fields.size()));
        sql.setLength(sql.length() - 1); // remove trailing comma
        sql.append(")");

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int index = 1;
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(entity);

                if (value != null) {
                    // extragere ID pentru entitati legate
                    switch (field.getType().getSimpleName()) {
                        case "User":
                            stmt.setObject(index++, ((classes.User) value).getNume(), Types.VARCHAR);
                            continue;
                        case "Plata":
                            stmt.setObject(index++, ((classes.Plata) value).getcod(), Types.VARCHAR);
                            continue;
                        /*case "Notificare":
                             stmt.setObject(index++, ((classes.Notificare) value).getId(), Types.VARCHAR);
                             continue;*/
                        case "Event":
                            stmt.setObject(index++, ((classes.Event) value).getNume(), Types.VARCHAR);
                            continue;
                    }
                }

                // fallback generic
                stmt.setObject(index++, value);
            }


            stmt.executeUpdate();
        }
    }

    //cautare dupa un anumit camp
    public Optional<T> findByField(Object valoare, String camp) throws Exception {
        String tableName = type.getSimpleName().toLowerCase() + "s";
        String sql = "SELECT * FROM " + tableName + " WHERE " + camp + " = ?";

        try (Connection conn = DbConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, valoare);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    T obj = type.getDeclaredConstructor().newInstance();
                    for (Field field : type.getDeclaredFields()) {
                        field.setAccessible(true);
                        if (List.class.isAssignableFrom(field.getType()) ||
                                Set.class.isAssignableFrom(field.getType()) ||
                                field.getType().isAssignableFrom(Artist.class)) {
                            continue;
                        }

                        Object val = rs.getObject(field.getName());

                        if (val instanceof java.sql.Date && field.getType() == LocalDate.class) {
                            val = ((java.sql.Date) val).toLocalDate();
                        }
                        else if (val instanceof java.sql.Timestamp && field.getType() == LocalDateTime.class) {
                            val = ((java.sql.Timestamp) val).toLocalDateTime();
                        }
                        else if (val instanceof Integer && field.getType() == Boolean.TYPE) {
                            val = ((Integer) val) != 0;
                        }
                        else if (val instanceof BigDecimal && field.getType() == Double.TYPE) {
                            val = ((BigDecimal) val).doubleValue();
                        }

                        field.set(obj, val);
                    }
                    return Optional.of(obj);
                }
            }
        }

        return Optional.empty();
    }

    //read
    public List<T> getObjects() throws Exception {
        List<T> list = new ArrayList<>();
        String sql = "SELECT * FROM " + type.getSimpleName().toLowerCase() + "s";

        try (Connection conn = DbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                T obj = type.getDeclaredConstructor().newInstance();
                for (Field field : type.getDeclaredFields()) {
                    field.setAccessible(true);

                    //ignor listele
                    Class<?> fieldType = field.getType();
                    if (List.class.isAssignableFrom(fieldType) ||
                            Set.class.isAssignableFrom(fieldType) ||
                            fieldType.isAssignableFrom(Artist.class)) {
                        continue;
                    }

                    Object value = rs.getObject(field.getName());

                    // conversii speciale
                    if (value instanceof java.sql.Date && field.getType() == LocalDate.class) {
                        value = ((java.sql.Date) value).toLocalDate();
                    }
                    else if (value instanceof java.sql.Timestamp && field.getType() == LocalDateTime.class) {
                        value = ((java.sql.Timestamp) value).toLocalDateTime();
                    }
                    else if (value instanceof Integer && field.getType() == Boolean.TYPE) {
                        value = ((Integer) value) != 0; // pentru boolean stocat ca int
                    }
                    else if (value instanceof BigDecimal && field.getType() == Double.TYPE) {
                        value = ((BigDecimal) value).doubleValue();
                    }

                    field.set(obj, value);
                }
                list.add(obj);
            }
        }
        return list;
    }

    // metode pentru update/delete ulterior
}
