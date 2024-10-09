package disaster_visualizer.model;

import java.util.List;
import java.util.Map;

public class Relation {

    final private String type = "Relation";
    private long id;
    private List<RelationMember> members;
    private Map<String, String> tags;

    public String getType() {
        return type;
    }

    public long getId() {
        return id;
    }

    public List<RelationMember> getMembers() {
        return members;
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTags(Map<String, String> tags) {
        this.tags = tags;
    }

}
