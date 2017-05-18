package analyzer.model.xml;

/**
 * Created by NM.Rabotaev on 09.05.2017.
 */
public class XmlProperty {

    private String name;
    private String ref;
    private String value;

    //ACCESSORS
    public String getName() {
        return name;
    }
    public String getRef() {
        return ref;
    }
    public String getValue() {
        return value;
    }
    public void setName(String name) {this.name = name;}
    public void setRef(String ref) {
        this.ref = ref;
    }
    public void setValue(String value) {
        this.value = value;
    }

}
