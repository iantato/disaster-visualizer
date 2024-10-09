package disaster_visualizer.model;

import java.util.Map;

public class Way {

    private String type;
    private long id;
    private long[] nodes;
    private Map<String, String> tags;

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public long[] getNodes() {
        return nodes;
    }

    public Map<String, String> getTags() {
        return tags;
    }

}
