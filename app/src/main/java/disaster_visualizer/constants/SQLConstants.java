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

    /**
     * Queries for data in the SQLite3 database.
     */
    public static final String QUERY_POSTALS = """
                SELECT id FROM "Tags"
                WHERE value in (?) AND type = "Relation"
            """;

    public static final String QUERY_RELATION_MEMBERS = """
                SELECT type, refId, role FROM "RelationMembers"
                WHERE relId = (?)
            """;

    public static final String QUERY_WAY_NODES = """
                SELECT nodeId FROM "WayNodes"
                WHERE wayId = (?)
            """;

    // public static final String QUERY_NODES = """
    //             SELECT longitude, latitude FROM "Nodes"
    //             WHERE value = (?)
    //         """;

}
