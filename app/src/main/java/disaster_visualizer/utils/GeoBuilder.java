// package disaster_visualizer.utils;

// import java.io.File;
// import java.sql.Connection;
// import java.sql.DriverManager;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.ResultSetMetaData;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.Arrays;

// import de.topobyte.osm4j.core.model.iface.OsmNode;
// import de.topobyte.osm4j.core.model.iface.OsmWay;
// import disaster_visualizer.constants.PostalCodes;
// import disaster_visualizer.constants.SQLConstants;

// public class GeoBuilder {

//     String database;

//     // Temporary as our scope is only cavite.
//     int[] postals = PostalCodes.CAVITE;
//     // Change to a lower digit on production.
//     final int cache_size = 1_000_000;

//     /**
//      *
//      *
//      * @param database
//      */
//     public GeoBuilder(String database) {
//         this.database = database;

//         if (!new File(database).exists()) {
//             System.out.println("No existing parsed PBF data to read..");
//             return;
//         }
//     }

//     /**
//      *
//      * @param pbfFile
//      * @param database
//      */
//     public GeoBuilder(String pbfFile, String database) {
//         this.database = database;

//         if (!new File(database).exists()) {
//             PbfReader pbfReader = new PbfReader(pbfFile, database);
//             pbfReader.storeAllSQL();
//         }
//     }

//     /**
//      * Creates an SQLite3 Database connection. It also
//      * configures and optimizes the database connection.
//      *
//      * @return SQLite3 Database connection.
//      */
//     public Connection createSQLConnection() {
//         try {
//             Connection connection = DriverManager.getConnection("jdbc:sqlite:" + database);
//             Statement statement = connection.createStatement();

//             // SQL Settings
//             statement.execute("PRAGMA journal_mode = OFF");
//             statement.execute("PRAGMA synchronous = 0");
//             statement.execute("PRAGMA locking_mode = EXCLUSIVE");
//             statement.execute("PRAGMA cache_size = " + cache_size);
//             statement.execute("PRAGMA temp_store = MEMORY");
//             connection.setAutoCommit(false);

//             return connection;
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return null;
//         }
//     }

//     /**
//      * Disconnects the SQLite3 Database connection. Always
//      * remember to disconnect the database to save the data.
//      *
//      * @param connection SQLite3 Database connection.
//      */
//     public void disconnectSQLConnection(Connection connection) {
//         try {
//             if (connection != null) {
//                 connection.commit();
//                 connection.close();
//             }
//         } catch (SQLException e) {
//             e.printStackTrace();
//             return;
//         }
//     }

//     /**
//      * Get relation data from Postal codes.
//      *
//      * @return;
//      */
//     public void getPostalGeo() {
//         Connection connection = this.createSQLConnection();

//         try {
//             Statement statement = connection.createStatement();
//             String sql = SQLConstants.QUERY_POSTALS.replace("?", Arrays.toString(PostalCodes.CAVITE)
//                                                                               .replace("[", "")
//                                                                               .replace("]", ""));
//             PreparedStatement nodeQuery = connection.prepareStatement(SQLConstants.QUERY_NODES);

//             ResultSet result = statement.executeQuery(sql);

//             while (result.next()) {
//                 switch (result.getString(1)) {
//                     case "Node":
//                         nodeQuery.setLong(1, result.getLong(2));
//                         break;
//                     case "Way":
//                         break;
//                     case "Relation":
//                         break;
//                 }
//             }


//         } catch (SQLException e) {
//             e.printStackTrace();
//         }

//         this.disconnectSQLConnection(connection);
//     }

//     public static void main(String[] args) {
//         GeoBuilder geoBuilder = new GeoBuilder("map.db");
//         geoBuilder.getPostal();
//     }
// }
