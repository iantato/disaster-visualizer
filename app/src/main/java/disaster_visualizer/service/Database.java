package disaster_visualizer.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import disaster_visualizer.constants.SQLConstants;

public class Database {

    // Change to a lower digit on production.
    final public static int cache_size = 1_000_000;

    /**
     * Creates the SQLite3 Tables if they do not exist yet.
     */
    public static void createSQLTables(String database) {
        Connection connection = createSQLConnection(database);

        try {
            Statement statement = connection.createStatement();

            for (String sql : SQLConstants.TABLES_CREATE) {
                statement.execute(sql);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        disconnectSQLConnection(connection);
    }

    /**
     * Creates an SQLite3 Database connection. It also
     * configures and optimizes the database connection.
     *
     * @return SQLite3 Database connection.
     */
    public static Connection createSQLConnection(String database) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:" + database);
            Statement statement = connection.createStatement();

            // SQL Settings
            statement.execute("PRAGMA journal_mode = OFF");
            statement.execute("PRAGMA synchronous = 0");
            statement.execute("PRAGMA locking_mode = EXCLUSIVE");
            statement.execute("PRAGMA cache_size = " + cache_size);
            statement.execute("PRAGMA temp_store = MEMORY");
            connection.setAutoCommit(false);

            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Disconnects the SQLite3 Database connection. Always
     * remember to disconnect the database to save the data.
     *
     * @param connection SQLite3 Database connection.
     */
    public static void disconnectSQLConnection(Connection connection) {
        try {
            if (connection != null) {
                connection.commit();
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }
    }

    /**
     * <b>long[] Array</b> insertion into queries for arrays.
     * This inserts arrays into queries that uses the "IN" keyword
     * as PreparedStatements does not work as of current testing.
     *
     * @param query The Statement's Query string.
     * @param array The array to be inserted.
     * @return Query with the inserted Array.
     */
    public static String queryArrays(String query, long[] array) {
        return query.replace("?", Arrays.toString(array)
                                               .replace("[", "(")
                                               .replace("]", ")"));
    }

}
