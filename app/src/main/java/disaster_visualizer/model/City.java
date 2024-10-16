package disaster_visualizer.model;

import java.util.LinkedList;

public class City {

    private String city;

    // Used for finding the centroid of the boundary.
    private double minLatitude = Double.MAX_VALUE;
    private double maxLatitude;

    private double minLongitude = Double.MAX_VALUE;
    private double maxLongitude;

    /**
     * This will the raw transposed value of Longitudes and Latitudes. For these
     * to be usable, it should be scaled into the proper values.
     *
     * X:
     * We can do the scaling by dividing the panel width with the difference
     * of the full 180° and -180° of the whole Earth (Width of the Earth),
     * converted into Mercator Projection.
     *   panelWidth / mercatorX(180°) - mercatorX(-180°)
     *
     * Y:
     * We can do the scaling by dividing the panel height with the difference
     * of the full 85° and -85° of the whole Earth (Height of the Earth),
     * converted into Mercator Projection.
     *   panelHeight / mercatorY(85°) - mercatorY(-85°)
     */
    private LinkedList<double[]> coordinates = new LinkedList<double[]>();

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLatitude(double minLatitude) {
        this.minLatitude = minLatitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLatitude(double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public void setMinLongitude(double minLongitude) {
        this.minLongitude = minLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public void setMaxLongitude(double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    public LinkedList<double[]> getCoordinates() {
        return coordinates;
    }

    public void addCoordinates(double[] c) {
        coordinates.add(c);
    }
}
