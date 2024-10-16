package disaster_visualizer.model;

import java.util.HashMap;

public class Node {

    final private String type = "Node";
    private long id;
    private double longitude;
    private double latitude;
    private HashMap<String, Object> tags = new HashMap<String, Object>();

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public HashMap<String, Object> getTags() {
        return tags;
    }

    public void addTag(String key, Object value) {
        tags.put(key, value);
    }


}
