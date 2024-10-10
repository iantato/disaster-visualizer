package disaster_visualizer.utils;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import de.topobyte.osm4j.core.model.iface.EntityContainer;
import de.topobyte.osm4j.core.model.iface.EntityType;
import de.topobyte.osm4j.core.model.iface.OsmNode;
import de.topobyte.osm4j.core.model.iface.OsmRelation;
import de.topobyte.osm4j.core.model.iface.OsmWay;
import de.topobyte.osm4j.core.model.impl.RelationMember;
import de.topobyte.osm4j.core.model.util.OsmModelUtil;
import de.topobyte.osm4j.pbf.seq.PbfIterator;
import disaster_visualizer.constants.SQLConstants;

public class PbfReader {
    String pbfFile;
    String database;

    private int currentNode, currentWay, currentRelation;
    private int maxNode, maxWay, maxRelation;

    // Change if you want to print the max sizes.
    final boolean countPrintable = false;

    /**
     * Parser and reader of PBF files. This creates a
     * SQLite3 database if one doesn't exist already.
     *
     * @param pbfFile File name of the pbf file.
     * @param database Name of the database.
     */
    public PbfReader(String pbfFile, String database) {
        this.pbfFile = pbfFile;
        this.database = database;

        if (!new File(database).exists()) {
            Database.createSQLTables(database);
        }
    }

    /**
     * Resets the InputStream as the built-in reset function
     * does not work with osm4j.
     *
     * @return InputStream of the PBF file.
     */
    public InputStream resetInputStream() {
        return PbfReader.class.getClassLoader().getResourceAsStream(String.format("map\\%s", pbfFile));
    }

    /**
     * Counts the elements inside the PBF file. Mainly used
     * for checking the progress of parsers.
     *
     * @param printable Print the counted elements.
     */
    public void countElements(boolean printable) {
        PbfIterator iter = new PbfIterator(this.resetInputStream(), false);

        System.out.println("Counting elements... Please wait.");
        for (EntityContainer container : iter) {
            switch (container.getType()) {
                case Node:
                    maxNode++;
                    break;
                case Way:
                    maxWay++;
                    break;
                case Relation:
                    maxRelation++;
                    break;
                default:
                    break;
            }
        }

        if (printable) {
            System.out.println("Number of Nodes: " + maxNode);
            System.out.println("Number of Ways: " + maxWay);
            System.out.println("Number of Relations: " + maxRelation);
        }
    }

    /**
     * Parses the nodes and stores it into the SQLite3 database file.
     * This uses a batch statement with other optimizations from the
     * SQLite3 connection in order to process large amounts of data.
     */
    public void storeNodesSQL() {
        if (maxNode == 0) {
            this.countElements(countPrintable);
        }

        PbfIterator iter = new PbfIterator(this.resetInputStream(), false);
        Connection connection = Database.createSQLConnection(database);

        try {
            PreparedStatement nodePreparedStatement = connection.prepareStatement(SQLConstants.NODES_INSERT);
            PreparedStatement tagsPreparedStatement = connection.prepareStatement(SQLConstants.TAGS_INSERT);

            for (EntityContainer container : iter) {
                if (container.getType() == EntityType.Node) {
                    System.out.print("Node added to batch: " + (currentNode + 1) + "/" + maxNode + "\r");
                    OsmNode node = (OsmNode) container.getEntity();

                    nodePreparedStatement.setString(1, node.getType().toString());
                    nodePreparedStatement.setLong(2, node.getId());
                    nodePreparedStatement.setDouble(3, node.getLongitude());
                    nodePreparedStatement.setDouble(4, node.getLatitude());

                    nodePreparedStatement.addBatch();

                    for (Map.Entry<String, String> set : OsmModelUtil.getTagsAsMap(node).entrySet()) {
                        tagsPreparedStatement.setString(1, node.getType().toString());
                        tagsPreparedStatement.setLong(2, node.getId());
                        tagsPreparedStatement.setString(3, set.getKey());
                        tagsPreparedStatement.setString(4, set.getValue());

                        tagsPreparedStatement.addBatch();
                    }

                    currentNode++;
                }
            }

            nodePreparedStatement.executeBatch();
            tagsPreparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("\n");
        Database.disconnectSQLConnection(connection);
    }

    /**
     * Parses the ways and stores it into the SQLite3 database file.
     * This uses a batch statement with other optimizations from the
     * SQLite3 connection in order to process large amounts of data.
     */
    public void storeWaysSQL() {
        if (maxWay == 0) {
            this.countElements(countPrintable);
        }

        PbfIterator iter = new PbfIterator(this.resetInputStream(), false);
        Connection connection = Database.createSQLConnection(database);

        try {
            PreparedStatement waysPreparedStatement = connection.prepareStatement(SQLConstants.WAYS_INSERT);
            PreparedStatement nodesPreparedStatement = connection.prepareStatement(SQLConstants.WAYS_NODES_INSERT);
            PreparedStatement tagsPreparedStatement = connection.prepareStatement(SQLConstants.TAGS_INSERT);

            for (EntityContainer container : iter) {
                if (container.getType() == EntityType.Way) {
                    System.out.print("Way added to batch: " + (currentWay + 1) + "/" + maxWay + "\r");
                    OsmWay way = (OsmWay) container.getEntity();

                    waysPreparedStatement.setString(1, way.getType().toString());
                    waysPreparedStatement.setLong(2, way.getId());

                    waysPreparedStatement.addBatch();

                    for (long nodeId : OsmModelUtil.nodesAsList(way).toArray()) {
                        nodesPreparedStatement.setLong(1, way.getId());
                        nodesPreparedStatement.setLong(2, nodeId);

                        nodesPreparedStatement.addBatch();
                    }

                    for (Map.Entry<String, String> set : OsmModelUtil.getTagsAsMap(way).entrySet()) {
                        tagsPreparedStatement.setString(1, way.getType().toString());
                        tagsPreparedStatement.setLong(2, way.getId());
                        tagsPreparedStatement.setString(3, set.getKey());
                        tagsPreparedStatement.setString(4, set.getValue());

                        tagsPreparedStatement.addBatch();
                    }

                    currentWay++;
                }
            }

            waysPreparedStatement.executeBatch();
            nodesPreparedStatement.executeBatch();
            tagsPreparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.print("\n");
        Database.disconnectSQLConnection(connection);
    }

    /**
     * Parses the relations and stores it into the SQLite3 database file.
     * This uses a batch statement with other optimizations from the
     * SQLite3 connection in order to process large amounts of data.
     */
    public void storeRelationsSQL() {
        if (maxRelation == 0) {
            this.countElements(countPrintable);
        }

        PbfIterator iter = new PbfIterator(this.resetInputStream(), false);
        Connection connection = Database.createSQLConnection(database);

        try {
            PreparedStatement relationsPreparedStatement = connection.prepareStatement(SQLConstants.RELATIONS_INSERT);
            PreparedStatement relationsMembersPreparedStatement = connection.prepareStatement(SQLConstants.RELATION_MEMBERS_INSERT);
            PreparedStatement tagsPreparedStatement = connection.prepareStatement(SQLConstants.TAGS_INSERT);

            for (EntityContainer container : iter) {
                if (container.getType() == EntityType.Relation) {
                    System.out.print("Relation added to batch: " + (currentRelation + 1) + "/" + maxRelation + "\r");
                    OsmRelation relation = (OsmRelation) container.getEntity();

                    relationsPreparedStatement.setString(1, relation.getType().toString());
                    relationsPreparedStatement.setLong(2, relation.getId());

                    relationsPreparedStatement.addBatch();

                    for (Object object : OsmModelUtil.membersAsList(relation)) {
                        RelationMember relationMember = (RelationMember) object;

                        relationsMembersPreparedStatement.setLong(1, relation.getId());
                        relationsMembersPreparedStatement.setString(2, relationMember.getType().toString());
                        relationsMembersPreparedStatement.setLong(3, relationMember.getId());
                        relationsMembersPreparedStatement.setString(4, relationMember.getRole());

                        relationsMembersPreparedStatement.addBatch();
                    }

                    for (Map.Entry<String, String> set : OsmModelUtil.getTagsAsMap(relation).entrySet()) {
                        tagsPreparedStatement.setString(1, relation.getType().toString());
                        tagsPreparedStatement.setLong(2, relation.getId());
                        tagsPreparedStatement.setString(3, set.getKey());
                        tagsPreparedStatement.setString(4, set.getValue());

                        tagsPreparedStatement.addBatch();
                    }

                    currentRelation++;
                }
            }

            relationsPreparedStatement.executeBatch();
            relationsMembersPreparedStatement.executeBatch();
            tagsPreparedStatement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("\n");
        Database.disconnectSQLConnection(connection);
    }

    /**
     * A single function that will run all 3 functions and
     * store them into the SQLite3 Database in the proper order.
     */
    public void storeAllSQL() {
        this.storeNodesSQL();
        this.storeWaysSQL();
        this.storeRelationsSQL();
    }
}
