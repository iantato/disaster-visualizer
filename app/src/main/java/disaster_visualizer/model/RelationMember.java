package disaster_visualizer.model;

public class RelationMember {

    private String type;
    private long ref;
    private String role;

    public String getType() {
        return type;
    }

    public long getRef() {
        return ref;
    }

    public String getRole() {
        return role;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setRef(long ref) {
        this.ref = ref;
    }

    public void setRole(String role) {
        this.role = role;
    }

}