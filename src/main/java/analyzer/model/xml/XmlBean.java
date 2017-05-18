package analyzer.model.xml;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NM.Rabotaev on 10.05.2017.
 */
public class XmlBean {
    private String id;
    private String className;
    private List<XmlProperty> properties;

    public XmlBean() {
        super();
        properties = new ArrayList<>();
    }

    //ACCESSORS
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    public List<XmlProperty> getProperties() {
        return properties;
    }
    public void addProperty(XmlProperty property) {
        getProperties().add(property);
    }
}
