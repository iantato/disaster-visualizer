package disaster_visualizer.model;

import java.util.HashMap;
import java.util.LinkedList;

public class Way {

    final private String type = "Ways";
    private long id;
    private LinkedList<Long> nodes = new LinkedList<Long>();
    private HashMap<String, String> tags = new HashMap<String, String>();

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LinkedList<Long> getNodes() {
        return nodes;
    }

    public void addNodes(long nodeID) {
        nodes.add(nodeID);
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void addTag(String key, String value) {
        tags.put(key, value);
    }



}
