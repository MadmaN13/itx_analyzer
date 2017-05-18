package analyzer.model;

/**
 * Created by NM.Rabotaev on 18.05.2017.
 */
public class Entity {
    private String name;
    private int discriminatorColumnLength;
    private InheritanceType inheritanceType;

    public Entity(String name) {
        this.name = name;
        discriminatorColumnLength = 0;
    }

    public enum InheritanceType {
        SINGLE_TABLE;
    }

    // ACCESSORS
    public String getName() {
        return name;
    }
    public int getDiscriminatorColumnLength() {
        return discriminatorColumnLength;
    }

    public InheritanceType getInheritanceType() {
        return inheritanceType;
    }
    public void setDiscriminatorColumnLength(int discriminatorColumnLength) {
        this.discriminatorColumnLength = discriminatorColumnLength;
    }

    public void setInheritanceType(InheritanceType inheritanceType) {
        this.inheritanceType = inheritanceType;
    }
}
