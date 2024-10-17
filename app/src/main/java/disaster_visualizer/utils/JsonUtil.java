package disaster_visualizer.utils;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtil {

    String json;

    final ObjectMapper mapper = new ObjectMapper();
    final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();

    /**
     * For creating or manipulating JSON files. Used mainly
     * for caching used map data.
     *
     * @param json
     */
    public JsonUtil(String json) {
        this.json = json;

        if (!new File(json).exists()) {
            this.initializeJson();
        }
    }

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
            writer.writeValue(new File(json), rootNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
// public class JsonUtil {

//     String json;
//     String database;

//     final int cache_size = 1_000_000;
//     final ObjectMapper mapper = new ObjectMapper();
//     final ObjectWriter writer = mapper.writer().withDefaultPrettyPrinter();
//     /**
//      * For creating JSON or reading JSON files.
//      * Use this function if you already have a database
//      * of OpenStreetMap data initialized.
//      *
//      * @param json Name of the json to read or edit (Add the .json extension on the String).
//      * @param database Name of the database (Add the .db extension on the String).
//      */
//     public JsonUtil(String json, String database) {
//         this.json = json;
//         this.database = database;

//         if (!new File(database).exists()) {
//             System.out.println("Database does not exist..");
//         }
//         if (!new File(json).exists()) {
//             this.initializeJson();
//         }
//     }

//     /**
//      * For creating JSON or reading JSON files.
//      * Use this function if there are no database
//      * available in your current setup.
//      *
//      * @param json Name of the json file to read or edit (Add the .json extension on the String).
//      * @param pbfFile Name of the PBF file (Add the .osm.pbf extension on the String).
//      * @param database Name of the database (Add the .db extension on the String).
//      */
//     @SuppressWarnings("unused")
//     public JsonUtil(String json, String pbfFile, String database) {
//         this.json = json;
//         this.database = database;

//         if (!new File(database).exists()) {
//             PbfReader pbfReader = new PbfReader(pbfFile, database);
//         }
//         if (!new File(json).exists()) {
//             this.initializeJson();
//         }
//     }

//     /**
//      * Initialize the JSON file that we'll be manipulating if
//      * it hasn't been created yet.
//      */
//     public void initializeJson() {
//         double version = 0.1;
//         String generator = String.format("Disaster Visualizer %s powered by osm4j", version);
//         String copyright = "The data included in this document is from www.OpenStreetMap.org. The data is made available under ODbL.";
//         ArrayNode elements = new ObjectMapper().createArrayNode();

//         /*
//          * Example:
//          *  {
//          *   "version": 0.1,
//          *   "generator": "Disaster Visualizer...",
//          *   "copyright": "The data included...",
//          *   "elements": []
//          *  }
//          */
//         ObjectNode rootNode = mapper.createObjectNode();
//         rootNode.put("version", version);
//         rootNode.put("generator", generator);
//         rootNode.put("copyright", copyright);
//         rootNode.set("elements", elements);

//         try {
//             writer.writeValue(new File(json), rootNode);
//         } catch (IOException e) {
//             e.printStackTrace();
//         }
//     }

//     // public void getRelations(Connection connection) {

//     //     LinkedList<Relation> relations = new LinkedList<Relation>();
//     //     LinkedList<Node> nodes = new LinkedList<Node>();

//     //     try {
//     //         Statement statement = connection.createStatement();
//     //         String sql = SQLConstants.QUERY_POSTALS.replace("?", Arrays.toString(PostalCodes.CAVITE)
//     //                                                                    .replace("[", "")
//     //                                                                    .replace("]", ""));

//     //         ResultSet result = statement.executeQuery(sql);

//     //         while (result.next()) {
//     //             long relationId = result.getLong(1);
//     //             LinkedList<RelationMember> relationMembers = getRelationMembers(connection, relationId);

//     //             Relation relation = new Relation();
//     //             relation.setId(relationId);

//     //             for (RelationMember relationMember : relationMembers) {
//     //                 switch (relationMember.getType()) {
//     //                     case "Node":
//     //                         break;

//     //                     case "Way":
//     //                         this.getWayNodes(connection, relationMember.getRef());
//     //                         break;

//     //                     default:
//     //                         break;
//     //                 }
//     //             }
//     //         }


//     //     } catch (SQLException e) {
//     //         e.printStackTrace();
//     //         return;
//     //     }

//     //     Database.disconnectSQLConnection(connection);
//     // }

//     // public LinkedList<RelationMember> getRelationMembers(Connection connection, long relationId) {

//     //     LinkedList<RelationMember> relationMembers = new LinkedList<RelationMember>();

//     //     try {
//     //         PreparedStatement statement = connection.prepareStatement(SQLConstants.QUERY_RELATION_MEMBERS);
//     //         statement.setLong(1, relationId);

//     //         ResultSet result = statement.executeQuery();

//     //         while (result.next()) {
//     //             RelationMember relationMember = new RelationMember();

//     //             relationMember.setType(result.getString(1));
//     //             relationMember.setRef(result.getLong(2));
//     //             relationMember.setRole(result.getString(3));

//     //             relationMembers.add(relationMember);
//     //         }

//     //         statement.close();

//     //     } catch (SQLException e) {
//     //         e.printStackTrace();
//     //         return null;
//     //     }

//     //     return relationMembers;
//     // }

//     // public void getWayNodes(Connection connection, long referenceId) {



//         // try {
//         //     PreparedStatement statement = connection.prepareStatement(SQLConstants.QUERY_WAY_NODES);
//         //     statement.setLong(1, referenceId);

//         // } catch (SQLException e) {
//         //     e.printStackTrace();
//         //     return;
//         // }
//     // }

//     // public void getNode(Connection connection, long nodeId) {

//     //     try {
//     //         PreparedStatement statement = connection.prepareStatement(database);
//     //     } catch (SQLException e) {

//     //     }

//     // }

//     // public void getWay() {}

//     // public void getTags() {

//     // }

//     // public void test() {
//     }

//     // public ResultSet getRelationIds() {
//     //     Connection connection = createSQLConnection();

//     //     ResultSet result;

//     //     try {
//     //         Statement statement = connection.createStatement();
//     //         String sql = SQLConstants.QUERY_POSTALS.replace(
//     //                                         "?",
//     //                                                 Arrays.toString(PostalCodes.CAVITE))
//     //                                                 .replace("[", "(")
//     //                                                 .replace("]", ")");

//     //         result = statement.executeQuery(sql);
//     //     } catch (SQLException e) {
//     //         e.printStackTrace();
//     //         return null;
//     //     }

//     //     disconnectSQLConnection(connection);
//     //     return result;
//     // }


//     // public static void main(String[] args) {
//     //     JsonUtil jsonUtil = new JsonUtil("map.json", "calabarzon.osm.pbf", "map.db");
//     //     jsonUtil.test();
//     // }
// }
