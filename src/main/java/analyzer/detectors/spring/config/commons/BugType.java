package analyzer.detectors.spring.config.commons;

/**
 * Created by NM.Rabotaev on 10.05.2017.
 */
public enum BugType {
    UNDECLARED_DATA_SOURCE_USAGE("UNDECLARED_DATA_SOURCE_USAGE"),
    DIFFERENT_SPRING_CONFIG_FILE_NAME("DIFFERENT_SPRING_CONFIG_FILE_NAME"),
    NON_EXISTENT_CLASS_REFERENCE("NON_EXISTENT_CLASS_REFERENCE"),
    NON_EXISTENT_IN_CONTEXT_BEAN_REFERENCE("NON_EXISTENT_IN_CONTEXT_BEAN_REFERENCE"),
    NON_EXISTENT_SPRING_INTEGRATION_CHANNEL_REFERENCE("NON_EXISTENT_SPRING_INTEGRATION_CHANNEL_REFERENCE"),
    NON_EXISTENT_SPRING_INTEGRATION_CHANNEL_DECLARATION("NON_EXISTENT_SPRING_INTEGRATION_CHANNEL_DECLARATION"),
    SYSTEM_OUT_PRINTLN_INVOCATION("SYSTEM_OUT_PRINTLN_INVOCATION"),
    UNCLOSED_ENTITY_MANAGER("UNCLOSED_ENTITY_MANAGER"),
    UNCLOSED_SQL_CONNECTION("UNCLOSED_SQL_CONNECTION"),
    UNCLOSED_URL_CONNECTION("UNCLOSED_URL_CONNECTION"),
    NO_ENTITY_DEFAULT_CONSTRUCTOR("NO_ENTITY_DEFAULT_CONSTRUCTOR"),
    HQL_PARAMS_UNSET("PARAM_HQL_UNSET"),
    TOO_MANY_EAGER_FIELDS("TOO_MANY_EAGER_FIELDS"),
    TOO_LONG_SINGLE_TABLE_CHILD_NAME("TOO_LONG_SINGLE_TABLE_CHILD_NAME");

    private String value;
    private BugType(String value) {
        this.value = value;
    }
    public String getValue() {return this.value;}
}
