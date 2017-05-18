package analyzer.commons.xml;

/**
 * Created by NM.Rabotaev on 09.05.2017.
 */
public enum XmlTag {

    PROPERTY_OPEN("<property"),
    CLOSE_SHORT("/>"),
    PROPERTY_CLOSE_LONG("</property>"),
    BEAN_OPEN("<bean"),
    BEAN_CLOSE_LONG("</bean>"),
    CHAIN_OPEN("<.*:chain"),
    CHAIN_CLOSE_LONG("</.*:chain>"),
    JMS_MESSAGE_DRIVEN_CHANNEL_ADAPTER_OPEN("<.*:message-driven-channel-adapter"),
    JMS_MESSAGE_DRIVEN_CHANNEL_ADAPTER_CLOSE_LONG("</.*:message-driven-channel-adapter"),
    SERVICE_ACTIVATOR_OPEN("<.*:service-activator"),
    SERVICE_ACTIVATOR_CLOSE_LONG("</.*:service-activator>");


    private String value;

    private XmlTag(String value) {
        this.value = value;
    }

    public String getValue() {return this.value;}
}
