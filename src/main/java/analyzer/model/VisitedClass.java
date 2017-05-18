package analyzer.model;

/**
 * Created by NM.Rabotaev on 18.05.2017.
 */
public class VisitedClass {

    private String name;
    private Entity entity;

    public VisitedClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public Entity getEntity() {
        return entity;
    }
    public void setEntity(Entity entity) {
        this.entity = entity;
    }
}
