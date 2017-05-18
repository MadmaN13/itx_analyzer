package analyzer.detectors.spring.config;

import analyzer.commons.FileUtils;
import analyzer.commons.xml.XmlBugInstanceConstructor;
import analyzer.commons.xml.XmlParser;
import analyzer.commons.xml.XmlTagValue;
import analyzer.detectors.spring.config.commons.AbstractSpringConfigDetector;
import analyzer.detectors.spring.config.commons.BugType;
import analyzer.detectors.spring.config.commons.Constants;
import analyzer.model.xml.XmlConfigFile;
import analyzer.model.xml.generated.BugInstanceType;
import analyzer.model.xml.generated.ObjectFactory;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.List;

/**
 * Created by NM.Rabotaev on 11.05.2017.
 */
public class DifferentSpringConfigFileNameDetector extends AbstractSpringConfigDetector {

    private String BUG_FILE_PATH = "";
    private String SPRING_ELEMENT_NAME = "";

    public DifferentSpringConfigFileNameDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    public void sawBug() {
        List<XmlConfigFile> xmlFiles = getXmlFiles();
        for (XmlConfigFile xmlFile:xmlFiles) {
            if (xmlFile.isSpringConfig()) {
                boolean differs = false;
                if (xmlFile.isSpringBatchJob()) {
                    differs = compareFileAndDeclaredElementNames(XmlTagValue.SPRING_BATCH_JOB_ELEMENT_ID, xmlFile);
                }
                if (xmlFile.isSpringIntegrationService()) {
                    differs = compareFileAndDeclaredElementNames(XmlTagValue.SPRING_INTEGRATION_SERVICE_ELEMENT_ID, xmlFile);
                }
                if (differs) {
                    BUG_FILE_PATH = xmlFile.getXml().getAbsolutePath();
                    printMessage("Found difference between springConfig filename and declared spring element. Filename: " + BUG_FILE_PATH +
                    ". Spring element name: " + SPRING_ELEMENT_NAME);
                    reportBug();
                }
            }
        }

    }

    private boolean compareFileAndDeclaredElementNames(XmlTagValue elementIdValue,XmlConfigFile xmlFile) {
        boolean differs = false;
        String elementId = XmlParser.getFirstTagParamValue(elementIdValue.getValue(), xmlFile.getXml());
        String fileName = xmlFile.getXml().getName().replaceAll(Constants.XML_FILE_FLAG.getValue(),"");
        if (!elementId.equals(fileName)) {
            differs = true;
            SPRING_ELEMENT_NAME = elementId;
        }
        return  differs;
    }

    protected void reportBug()  {
        BugInstanceType bugInstance = createConfigBugInstance();
        try {
            String bugXml = getMarshaller().marshal(new ObjectFactory().createConfigBugInstance(bugInstance));
            FileUtils.writeBug(new File(Constants.REPORT_FILE.getValue()), bugXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BugInstanceType createConfigBugInstance()  {
        return XmlBugInstanceConstructor.bugInstance(
                this.getClass().getName(),
                BugType.DIFFERENT_SPRING_CONFIG_FILE_NAME.getValue(),
                BUG_FILE_PATH,
                "Found difference between springConfig filename and declared spring element. Filename: " + BUG_FILE_PATH +
                        ". Spring element name: " + SPRING_ELEMENT_NAME);
    }

    protected BugInstance createBug()throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
