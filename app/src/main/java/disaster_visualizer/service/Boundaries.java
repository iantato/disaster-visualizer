package disaster_visualizer.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import disaster_visualizer.constants.Cities;
import disaster_visualizer.constants.SQLConstants;
import disaster_visualizer.model.City;
import disaster_visualizer.model.Node;
import disaster_visualizer.model.Relation;
import disaster_visualizer.model.Way;
import disaster_visualizer.utils.MercatorProjection;

public class Boundaries {

    // Change for larger scope/area.
    String[] area = Cities.CAVITE;

    /**
     * This gets all the coordinates of the city borders from our
     * OpenStreetMap data. It will only store the data into transposed
     * Longitude and Latitude using Mercator Projection. Although, the
     * maximum and minimum of longitude and latitude will also be stored.
     *
     * @param connection SQLite3 Database Connection.
     * @throws SQLException if there is a problem with the queries
     *                      or in the connection.
     */
    public void getCityBorders(Connection connection) throws SQLException {

        PreparedStatement cityQuery = connection.prepareStatement(SQLConstants.CITY_QUERY);

        for (String cityName : area) {
            City city = new City();

            cityQuery.setString(1, cityName);
            ResultSet result = cityQuery.executeQuery();

            /*
             * Get the Relation object so that we can dissect the
             * Relation Members (Node, Ways, and other Relations).
             */
            Relation relation = RelationService.getRelation(connection, result.getLong(1));
            for (disaster_visualizer.model.RelationMember relationMember : relation.getMembers()) {

                /*
                 * Check if the Relation Member is a Way object and if the way is
                 * the one used for drawing a part of the boundary of a Relation.
                 * This way, we can filter the data into only giving us the
                 * Relation's boundaries for drawing in our application.
                 */
                if (relationMember.getType().equals("Way") && relationMember.getRole().equals("outer")) {
                    Way way = WayService.getWays(connection, relationMember.getRef());

                    // We don't want to draw ways that are 'maritime' or ocean boundaries.
                    if (way.getTags().get("maritime") != null) {
                        continue;
                    }

                    for (Long nodeID : way.getNodes()) {
                        Node node = NodeService.getNode(connection, nodeID);
                        double x = MercatorProjection.mercatorX(node.getLongitude());
                        double y = MercatorProjection.mercatorY(node.getLatitude());

                        // Set the latitude and transposed latitude's minimum and maximum.
                        city.setMinLatitude(Math.min(city.getMinLatitude(), node.getLatitude()));
                        city.setMaxLatitude(Math.max(city.getMaxLatitude(), node.getLatitude()));

                        // Set the longitude and transposed longitude's minimum and maximum.
                        city.setMinLongitude(Math.min(city.getMinLongitude(), node.getLongitude()));
                        city.setMaxLongitude(Math.max(city.getMaxLongitude(), node.getLongitude()));

                        city.addCoordinates(new double[] {x, y});
                    }
                }
            }
        }

        cityQuery.close();
    }

    // TODO
    // public void getBarangay() {}

    // TODO
    // public void getStreets() {}
}
