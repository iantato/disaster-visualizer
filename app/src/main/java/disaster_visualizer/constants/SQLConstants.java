package disaster_visualizer.constants;

public class SQLConstants {

    /**
     * SQLite3 Tables creation.
     */
    public static final String[] TABLES_CREATE = {
        """
            CREATE TABLE IF NOT EXISTS Nodes (
                type VARCHAR(16),
                id BIGINT(16) PRIMARY KEY NOT NULL,
                longitude DOUBLE(10, 6),
                latitude DOUBLE(10, 6)
            )
        """,
        """
            CREATE TABLE IF NOT EXISTS Ways (
                type VARCHAR(16),
                id BIGINT(16) PRIMARY KEY NOT NULL
            )
        """,
        """
            CREATE TABLE IF NOT EXISTS Relations (
                type VARCHAR(16),
                id BIGINT(16) PRIMARY KEY NOT NULL
            )
        """,
        """
            CREATE TABLE IF NOT EXISTS Tags (

                type VARCHAR(16) NOT NULL,
                id BIGINT(16) NOT NULL,
                key VARCHAR(16),
                value VARCHAR(16),

                CONSTRAINT fkType FOREIGN KEY (type) REFERENCES Nodes(type),
                CONSTRAINT fkType FOREIGN KEY (type) REFERENCES Ways(type),
                CONSTRAINT fkType FOREIGN KEY (type) REFERENCES Relations(type),

                CONSTRAINT fkId FOREIGN KEY (id) REFERENCES Nodes(id),
                CONSTRAINT fkId FOREIGN KEY (id) REFERENCES Ways(id),
                CONSTRAINT fkId FOREIGN KEY (id) REFERENCES Relations(id)
            )
        """,
        """
            CREATE TABLE IF NOT EXISTS WayNodes (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                wayId BIGINT(16) NOT NULL,
                nodeId BIGINT(16) NOT NULL,

                CONSTRAINT fkWayId FOREIGN KEY (wayId) REFERENCES Ways(id),

                CONSTRAINT fkNodeId FOREIGN KEY (nodeId) REFERENCES Nodes(id)
            )
        """,
        """
            CREATE TABLE IF NOT EXISTS RelationMembers (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                relId BIGINT(16) NOT NULL,
                type VARCHAR(16) NOT NULL,
                refId BIGINT(16) NOT NULL,
                role VARCHAR(16),

                CONSTRAINT fkRelId FOREIGN KEY (relId) REFERENCES Relations(id),

                CONSTRAINT fkType FOREIGN KEY (type) REFERENCES Nodes(type),
                CONSTRAINT fkType FOREIGN KEY (type) REFERENCES Ways(type),

                CONSTRAINT fkId FOREIGN KEY (refId) REFERENCES Nodes(id),
                CONSTRAINT fkId FOREIGN KEY (refId) REFERENCES Ways(id)
            )
        """,
        """
            CREATE TABLE IF NOT EXISTS Cities (
                relId BIGINT(16) PRIMARY KEY NOT NULL,
                minLongitude DOUBLE(10, 6),
                maxLongitude DOUBLE(10, 6),
                minLatitude DOUBLE(10, 6),
                maxLatitude DOUBLE(10, 6),

                CONSTRAINT fkRelId FOREIGN KEY (relId) REFERENCES Relations(id)
            )
        """,
        """
            CREATE TABLE IF NOT EXISTS TransposedCityBoundaries (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                relId BIGINT(16) NOT NULL,
                longitude DOUBLE(10, 6),
                latitude DOUBLE(10, 6),
                posX DOUBLE(10, 6),
                posY DOUBLE(10, 6),

                CONSTRAINT fkRelId FOREIGN KEY (relId) REFERENCES Relations(id)
            )
        """
    };

    /**
     * Insertion SQLite3 queries.
     */
    public static final String NODES_INSERT = """
                INSERT INTO Nodes
                (
                    type,
                    id,
                    longitude,
                    latitude
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?
                )
            """;

    public static final String WAYS_INSERT = """
                INSERT INTO Ways
                (
                    type,
                    id
                )
                VALUES
                (
                    ?,
                    ?
                )
            """;

    public static final String WAYS_NODES_INSERT = """
                INSERT INTO WayNodes
                (
                    wayId,
                    nodeId
                )
                VALUES
                (
                    ?,
                    ?
                )
            """;

    public static final String RELATIONS_INSERT = """
                INSERT INTO Relations
                (
                    type,
                    id
                )
                VALUES
                (
                    ?,
                    ?
                )
            """;

    public static final String RELATION_MEMBERS_INSERT = """
                INSERT INTO RelationMembers
                (
                    relId,
                    type,
                    refId,
                    role
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?
                )
            """;

    public static final String TAGS_INSERT = """
                INSERT INTO Tags
                (
                    type,
                    id,
                    key,
                    value
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?
                )
            """;

    public static final String CITY_INSERT = """
                INSERT INTO Cities
                (
                    relId,
                    minLongitude,
                    maxLongitude,
                    minLatitude,
                    maxLatitude
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?
                )
            """;

    public static final String TRANSPOSED_CITY_BOUNDARIES_INSERT = """
                INSERT INTO TransposedCityBoundaries (
                    relId,
                    longitude,
                    latitude,
                    posX,
                    posY
                )
                VALUES
                (
                    ?,
                    ?,
                    ?,
                    ?,
                    ?
                )
            """;


    /**
     * Updates for data in SQLite3 database.
     */
    // public static final String CITY_MIN_MAX_UPDATE = """
    //             UPDATE Cities
    //             SET minLongitude = (?),
    //                 maxLongitude = (?),
    //                 minLatitude = (?),
    //                 maxLatitude = (?)
    //             WHERE relId = (?)
    //         """;

    /**
     * Queries for data in the SQLite3 database.
     */
    // public static final String NODE_QUERY = """
    //             SELECT * FROM "Nodes"
    //             WHERE id = (?)
    //         """;

    // public static final String MULTIPLE_NODES_QUERY = """
    //             SELECT * FROM "Nodes"
    //             WHERE id IN ?
    //         """;

    // public static final String WAY_QUERY = """
    //             SELECT nodeId FROM "WayNodes"
    //             WHERE wayId = (?)
    //         """;

    // public static final String RELATION_QUERY = """
    //             SELECT type, refId, role FROM "RelationMembers"
    //             WHERE relId = (?)
    //         """;

    // public static final String TAG_QUERY = """
    //             SELECT key, value FROM "Tags"
    //             WHERE type = (?) AND id = (?)
    //         """;

    public static final String CITY_NAMES_QUERY = """
                SELECT id, value from "Tags"
                WHERE key = "name" AND value IN ? AND type = "Relation"
            """;

    public static final String NODES_QUERY = """
                SELECT RelationMembers.refId, Nodes.longitude, Nodes.latitude
                FROM "RelationMembers"
                JOIN "Nodes" ON RelationMembers.refId = Nodes.id
                WHERE RelationMembers.relId IN ? AND RelationMembers.type = "Node"
            """;

    // public static final String WAY_NODES_QUERY = """
    //             SELECT RelationMembers.relId, RelationMembers.role, Nodes.id, Nodes.longitude, Nodes.latitude
    //             FROM RelationMembers
    //             JOIN WayNodes ON RelationMembers.refId = WayNodes.WayId
    //             JOIN Nodes ON WayNodes.nodeId = Nodes.id
    //             WHERE RelationMembers.relId IN ? AND RelationMembers.type = "Way" AND RelationMembers.role = "outer"
    //             )
    //         """;

    public static final String WAY_NODES_QUERY = """
                SELECT RelationMembers.relId, RelationMembers.role, Nodes.id, Nodes.longitude, Nodes.latitude
                FROM RelationMembers
                JOIN WayNodes ON RelationMembers.refId = WayNodes.WayId
                JOIN Nodes ON WayNodes.nodeId = Nodes.id
                LEFT JOIN Tags ON WayNodes.WayId = Tags.id AND Tags.key = "maritime"
                WHERE RelationMembers.relId IN ? AND RelationMembers.type = "Way" AND RelationMembers.role = "outer" AND Tags.id IS NULL
            """;
}
