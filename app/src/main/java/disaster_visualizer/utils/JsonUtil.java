package disaster_visualizer.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import disaster_visualizer.constants.PostalCodes;
import disaster_visualizer.constants.SQLConstants;

public class JsonUtil {

    String json;
    String database;

    final int cache_size = 1_000_000;
    final ObjectMapper mapper = new ObjectMapper();
    final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

    Queue<Long> relationIds = new LinkedList<Long>();
    Queue<Long> wayIds = new LinkedList<Long>();
    Queue<Long> nodeIds = new LinkedList<Long>();

    /**
     * For creating JSON or reading JSON files.
     * Use this function if you already have a database
     * of OpenStreetMap data initialized.
     *
     * @param json Name of the json to read or edit.
     * @param database Name of the database.
     */
    public JsonUtil(String json, String database) {
        this.json = json;
        this.database = database;

        if (!new File(database).exists()) {
            System.out.println("Database does not exist..");
        }
        if (!new File("json\\" + json).exists()) {
            this.initializeJson();
        }
    }

    /**
     * For creating JSON or reading JSON files.
     * Use this function if there are no database
     * available in your current setup.
     *
     * @param json Name of the json file to read or edit.
     * @param pbfFile Name of the PBF file.
     * @param database Name of the database.
     */
    @SuppressWarnings("unused")
    public JsonUtil(String json, String pbfFile, String database) {
        this.json = json;
        this.database = database;

        if (!new File(database).exists()) {
            PbfReader pbfReader = new PbfReader(pbfFile, database);
        }
        if (!new File("json\\" + json).exists()) {
            this.initializeJson();
        }
    }

    /**
     * Creates an SQLite3 Database connection. It also
     * configures and optimizes the database connection.
     *
     * @return SQLite3 Database connection.
     */
    public Connection createSQLConnection() {
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
    public void disconnectSQLConnection(Connection connection) {
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
     * Initialize the JSON file that we'll be manipulating if
     * it hasn't been created yet.
     */
    public void initializeJson() {
        double version = 0.1;
        String generator = String.format("Disaster Visualizer %s powered by osm4j", version);
        String copyright = "The data included in this document is from www.OpenStreetMap.org. The data is made available under ODbL.";
        ArrayNode elements = new ObjectMapper().createArrayNode();

        /*
         * Example:
         *  {
         *   "version": 0.1,
         *   "generator": "Disaster Visualizer...",
         *   "copyright": "The data included...",
         *   "elements": []
         *  }
         */
        ObjectNode rootNode = mapper.createObjectNode();
        rootNode.put("version", version);
        rootNode.put("generator", generator);
        rootNode.put("copyright", copyright);
        rootNode.set("elements", elements);

        try {
            writer.writeValue(new File("json\\" + json), rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultSet getRelationIds() {
        Connection connection = createSQLConnection();

        ResultSet result;

        try {
            Statement statement = connection.createStatement();
            String sql = SQLConstants.QUERY_POSTALS.replace(
                                            "?",
                                                    Arrays.toString(PostalCodes.CAVITE))
                                                    .replace("[", "(")
                                                    .replace("]", ")");

            result = statement.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        disconnectSQLConnection(connection);
        return result;
    }

}
