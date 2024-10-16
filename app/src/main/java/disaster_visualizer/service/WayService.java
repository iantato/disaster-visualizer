package disaster_visualizer.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import disaster_visualizer.constants.SQLConstants;
import disaster_visualizer.model.Way;

public class WayService {

    /**
     * Gets the information of a single Way from the database and
     * turns it into a Way object so that we can store and read
     * the data.
     *
     * @param connection SQLite3 Database Connection.
     * @param wayID The ID of the Way.
     * @return Way object.
     * @throws SQLException if there is a problem with the queries
     *                      or in the connection.
     */
    public static Way getWays(Connection connection, long wayID) throws SQLException {

        // Initialize the Way object.
        Way way = new Way();

        PreparedStatement wayStatement = connection.prepareStatement(SQLConstants.WAY_QUERY);
        wayStatement.setLong(1, wayID);

        ResultSet result = wayStatement.executeQuery();
        way.setId(wayID);
        while (result.next()) {
            way.addNodes(result.getLong(1));
        }

        wayStatement.close();

        return way;
    }

    /**
     * Get the tags of a Way.
     *
     * @param connection SQLite3 Database Connection.
     * @param way The Node object to store the tags into.
     * @throws SQLException if there is a problem with the queries
     *                      or in the connection.
     */
    public static void getTags(Connection connection, Way way) throws SQLException {

        PreparedStatement tagsStatement = connection.prepareStatement(SQLConstants.TAG_QUERY);

        tagsStatement.setString(1, "Way");
        tagsStatement.setLong(2, way.getId());

        ResultSet result = tagsStatement.executeQuery();
        while (result.next()) {
            way.addTag(result.getString("key"), result.getString("value"));
        }

        tagsStatement.close();
    }
}
