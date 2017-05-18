package analyzer.commons.xml;

/**
 * Created by NM.Rabotaev on 10.05.2017.
 */
public enum XmlTagValue {
    DATASOURCE_CLASS_NAME("org.apache.commons.dbcp.BasicDataSource"),
    DATASOURCE_NAME("dataSource"),
    PROPERTY_NAME("name"),
    PROPERTY_VALUE("value"),
    PROPERTY_REF("ref"),
    BEAN_ID("id"),
    BEAN_CLASS("class"),
    SPRING_BATCH_JOB_ELEMENT_ID("job id"),
    SPRING_INTEGRATION_SERVICE_ELEMENT_ID("service-descriptor id"),
    SPRING_INTEGRATION_CHANNEL_ELEMENT_ID("channel id"),
    SPRING_INTEGRATION_CHANNEL_ELEMENT_REF("channel"),
    SPRING_INTEGRATION_INPUT_CHANNEL_ELEMENT_REF("input-channel"),
    SPRING_INTEGRATION_OUTPUT_CHANNEL_ELEMENT_REF("output-channel"),
    SPRING_INTEGRATION_DESTINATION_NAME("destination-name");
    private String value;

    private XmlTagValue(String value) {
        this.value = value;
    }

    public String getValue() {return this.value;}
}
