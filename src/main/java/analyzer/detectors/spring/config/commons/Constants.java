package analyzer.detectors.spring.config.commons;

/**
 * Created by NM.Rabotaev on 11.05.2017.
 */
public enum Constants {
    ANY_STRING(".*"),
    FILE_IS_SPRING_INTEGRATION_CONFIG_FLAG("http://www.springframework.org/schema/integration http://www.springframework.org/schema/integration/spring-integration.xsd"),
    FILE_IS_SPRING_BATCH_CONFIG_FLAG("http://www.springframework.org/schema/batch/spring-batch.xsd"),
    XML_FILE_FLAG(".xml"),
    JAVA_FILE_FLAG(".java"),
    ROOT("C:\\Users\\NM.Rabotaev\\IdeaProjects\\itx_analyzer\\src"),
    REPORT_FILE("C:\\Users\\NM.Rabotaev\\Desktop\\analyzerLogs\\config_bugs.txt"),
    GLOBAL_CONTEXT_FILE_NAME("global-context");
    ;


    private String value;

    private Constants(String value) {
        this.value = value;
    }

    public String getValue() {return this.value;}
}
