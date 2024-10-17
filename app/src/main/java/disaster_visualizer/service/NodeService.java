// package disaster_visualizer.service;

// import java.sql.Connection;
// import java.sql.PreparedStatement;
// import java.sql.ResultSet;
// import java.sql.SQLException;
// import java.sql.Statement;
// import java.util.LinkedList;

// import disaster_visualizer.constants.SQLConstants;
// import disaster_visualizer.model.Node;

// public class NodeService {

//     /**
//      * Get the information of a single Node from the database and
//      * turns it into a Node object so that we can store and
//      * read the data.
//      *
//      * @param connection SQLite3 Database Connection.
//      * @param nodeID The ID of the Node.
//      * @return Node object.
//      * @throws SQLException if there is a problem with the queries
//      *                      or in the connection.
//      */
//     public static Node getNode(Connection connection, long nodeID) throws SQLException {

//         // Initialize the Node object.
//         Node node = new Node();
//         // Prepared Statements for querying for the node information.
//         PreparedStatement nodeStatement = connection.prepareStatement(SQLConstants.NODE_QUERY);
//         nodeStatement.setLong(1, nodeID);

//         ResultSet result = nodeStatement.executeQuery();
//         node.setId(nodeID);
//         node.setLongitude(result.getDouble("longitude"));
//         node.setLatitude(result.getDouble("latitude"));
//         getTags(connection, node);

//         nodeStatement.close();

//         return node;
//     }

//     /**
//      * Get the information of multiple Nodes from the database and
//      * turns them all into a Node object so that we can store and
//      * read all of the data. We then store them all into a LinkedList
//      * so that we can easily access them.
//      *
//      * @param connection SQLite3 Database Connection.
//      * @param nodesID An array of all the Node IDs
//      * @return A linked list of all the Nodes.
//      * @throws SQLException if there is a problem with the queries
//      *                      or in the connection.
//      */
//     public static LinkedList<Node> getMultipleNodes(Connection connection, Long[] nodesID) throws SQLException {

//         // Uses a linked list of Nodes as we don't know the total size of the array.
//         LinkedList<Node> nodes = new LinkedList<Node>();

//         // Initializes the statement and the query.
//         Statement nodeStatement = connection.createStatement();
//         ResultSet result = nodeStatement.executeQuery(Database.queryArrays(SQLConstants.MULTIPLE_NODES_QUERY, nodesID));

//         // Store all the nodes into the Linked List.
//         while (result.next()) {
//             Node node = new Node();

//             node.setId(result.getLong("id"));
//             node.setLongitude(result.getDouble("longitude"));
//             node.setLatitude(result.getDouble("latitude"));
//             getTags(connection, node);

//             nodes.add(node);
//         }

//         nodeStatement.close();

//         return nodes;
//     }

//     /**
//      * Get the tags of a Node.
//      *
//      * @param connection SQLite3 Database Connection.
//      * @param node The Node object to store the tags into.
//      * @throws SQLException if there is a problem with the queries
//      *                      or in the connection.
//      */
//     public static void getTags(Connection connection, Node node) throws SQLException {

//         PreparedStatement tagsStatement = connection.prepareStatement(SQLConstants.TAG_QUERY);

//         // Tags PreparedStatement
//         tagsStatement.setString(1, "Node");
//         tagsStatement.setLong(2, node.getId());

//         ResultSet result = tagsStatement.executeQuery();
//         while (result.next()) {
//             // Add the tags into the Node's HashMap of the tags.
//             node.addTag(result.getString(1), result.getObject(2));
//         }

//         tagsStatement.close();
//     }
// }
