package disaster_visualizer.model;

import java.util.Map;

public class Node {

    private String type;
    private long id;
    private double longitude;
    private double latitude;
    private Map<String, String> tags;

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public Map<String, String> getTags() {
        return tags;
    }
}
