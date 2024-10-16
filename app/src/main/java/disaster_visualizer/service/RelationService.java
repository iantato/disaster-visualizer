package disaster_visualizer.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import disaster_visualizer.constants.SQLConstants;
import disaster_visualizer.model.Relation;
import disaster_visualizer.model.RelationMember;

public class RelationService {

    /**
     * Gets the information of a single Relation from the database and
     * turns it into a Relation object so that we can store and read
     * the data.
     *
     * @param connection SQLite3 Database Connection.
     * @param relationID The ID of the Relation.
     * @return Relation object.
     * @throws SQLException if there is a problem with the queries
     *                      or in the connection.
     */
    public static Relation getRelation(Connection connection, long relationID) throws SQLException {

        // Initialize the Relation object.
        Relation relation = new Relation();

        PreparedStatement relationStatement = connection.prepareStatement(SQLConstants.RELATION_QUERY);
        relationStatement.setLong(1, relationID);

        ResultSet result = relationStatement.executeQuery();
        relation.setId(relationID);

        // Adds the Relation Members into the Relation object.
        while (result.next()) {
            RelationMember relationMember = new RelationMember();

            relationMember.setType(result.getString("type"));
            relationMember.setRef(result.getLong("refId"));
            relationMember.setRole(result.getString("role"));

            relation.addMember(relationMember);
        }
        getTags(connection, relation);

        relationStatement.close();

        return relation;
    }

    /**
     * Get the tags of a Relation.
     *
     * @param connection SQLite3 Database Connection.
     * @param way The Node object to store the tags into.
     * @throws SQLException if there is a problem with the queries
     *                      or in the connection.
     */
    public static void getTags(Connection connection, Relation relation) throws SQLException {

        PreparedStatement tagsStatement = connection.prepareStatement(SQLConstants.TAG_QUERY);

        // Tags PreparedStatement
        tagsStatement.setString(1, "Relation");
        tagsStatement.setLong(2, relation.getId());

        ResultSet result = tagsStatement.executeQuery();
        while (result.next()) {
            // Add the tags into the Relation's HashMap of the tags.
            relation.addTag(result.getString("key"), result.getString("value"));
        }

        tagsStatement.close();

    }
}
