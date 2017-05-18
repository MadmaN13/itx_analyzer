package analyzer.model.xml;

import java.io.File;

/**
 * Created by NM.Rabotaev on 08.05.2017.
 */
public class XmlConfigFile {

    private File xml;
    private boolean isSpringConfig;
    private boolean isSpringBatchJob;
    private boolean isSpringIntegrationService;

    public XmlConfigFile(File xml) {
        this.xml = xml;
    }

    //ACCESSORS
    public File getXml() {
        return xml;
    }

    public boolean isSpringConfig() {
        return isSpringConfig;
    }

    public void setSpringConfig(boolean springConfig) {
        isSpringConfig = springConfig;
    }

    public boolean isSpringBatchJob() {
        return isSpringBatchJob;
    }

    public void setSpringBatchJob(boolean springBatchJob) {
        isSpringBatchJob = springBatchJob;
    }

    public boolean isSpringIntegrationService() {
        return isSpringIntegrationService;
    }

    public void setSpringIntegrationService(boolean springIntegrationService) {
        isSpringIntegrationService = springIntegrationService;
    }

    @Override
    public String toString() {
        return "XmlConfigFile:" + "\r\n" +
                "File path = " + xml.getAbsolutePath() + "\r\n" +
                "isSpringConfig = " + isSpringConfig + "\r\n" +
                "---------------------------------------" + "\r\n";
    }

}
