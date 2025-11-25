package org.example.util;

import java.sql.*;

public final class DbUtil {
    private static final String URL = "jdbc:sqlite:library.db";

    static {
        try { Class.forName("org.sqlite.JDBC"); }
        catch (ClassNotFoundException ex) { throw new RuntimeException("SQLite JDBC driver not found", ex); }
    }

    private DbUtil() {}

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void initDatabase() {
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS books ("
                    + "id INTEGER PRIMARY KEY, "
                    + "title TEXT NOT NULL, "
                    + "author TEXT, "
                    + "copies INTEGER DEFAULT 1, "
                    + "issued INTEGER DEFAULT 0"
                    + ");";
            st.execute(sql);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
