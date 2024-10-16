package disaster_visualizer.model;

import java.util.HashMap;
import java.util.LinkedList;

public class Relation {

    final private String type = "Relation";
    private long id;
    private LinkedList<RelationMember> members = new LinkedList<RelationMember>();
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

    public LinkedList<RelationMember> getMembers() {
        return members;
    }

    public void addMember(RelationMember member) {
        members.add(member);
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void addTag(String key, String value) {
        tags.put(key, value);
    }
}
