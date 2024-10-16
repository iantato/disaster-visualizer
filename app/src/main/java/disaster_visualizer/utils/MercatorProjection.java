package disaster_visualizer.utils;

/**
 * Uses the Mercator Projection formula in order to
 * transpose the Longitude and Latitudes into
 * X and Y values. This will be used for drawing
 * the map into the panels.
 */
public class MercatorProjection {

    // Approximate Earth's radius in kilometers
    private static final double radius = 6371;

    /**
     * Convert degrees into radians.
     *
     * @param degrees Longitude/Latitude.
     * @return Radians from Degrees.
     */
    public static double toRadians(double degrees) {
        return Math.toRadians(degrees);
    }

    /**
     * Calculate the X Coordinate (Longitude).
     *
     * @param longitude Longitude.
     * @return Transposed X coordinate.
     */
    public static double mercatorX(double longitude) {
        return radius * toRadians(longitude);
    }

    /**
     * Calculate the Y Coordinate (Latitude).
     *
     * @param latitude Latitude.
     * @return Transposed Y coordinate.
     */
    public static double mercatorY(double latitude) {
        double radiusLatitude = toRadians(latitude);
        return radius * Math.log(Math.tan(Math.PI / 4 + radiusLatitude / 2));
    }

}
