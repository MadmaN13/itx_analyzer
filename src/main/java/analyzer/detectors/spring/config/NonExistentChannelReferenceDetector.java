package analyzer.detectors.spring.config;

import analyzer.commons.FileUtils;
import analyzer.commons.xml.XmlBugInstanceConstructor;
import analyzer.commons.xml.XmlParser;
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
import java.util.Set;

/**
 * Created by NM.Rabotaev on 11.05.2017.
 */
public class NonExistentChannelReferenceDetector extends AbstractSpringConfigDetector {
    private String BUG_FILE_PATH = "";
    private String NON_EXISTENT_CHANNEL_ID = "";


    public NonExistentChannelReferenceDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    public void sawBug() {
        List<XmlConfigFile> xmlFiles = getXmlFiles();
        for (XmlConfigFile xmlFile:xmlFiles) {
            if (xmlFile.isSpringIntegrationService()) {
                File xml = xmlFile.getXml();
                List<String> declaredChannels = XmlParser.readDeclaredChannels(xml);
                // TODO: 12.05.2017 use Set<String> here
                Set<String> referencedChannels = XmlParser.readReferencedChannels(xml);
                for (String ref:referencedChannels) {
                    if (!declaredChannels.contains(ref)) {
                        BUG_FILE_PATH = xml.getAbsolutePath();
                        NON_EXISTENT_CHANNEL_ID = ref;
                        reportBug();
                    }
                }
            }
        }

    }

    protected void reportBug() {
        BugInstanceType bugInstance = createConfigBugInstance();
        try {
            String bugXml = getMarshaller().marshal(new ObjectFactory().createConfigBugInstance(bugInstance));
            FileUtils.writeBug(new File(Constants.REPORT_FILE.getValue()), bugXml);
            printMessage("Found reference to undeclared spring integration channel in service: " + BUG_FILE_PATH +
            ". Occurrence in file: channel=" + NON_EXISTENT_CHANNEL_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BugInstanceType createConfigBugInstance() {
        return XmlBugInstanceConstructor.bugInstance(
                this.getClass().getName(),
                BugType.NON_EXISTENT_SPRING_INTEGRATION_CHANNEL_REFERENCE.getValue(),
                BUG_FILE_PATH,
                "Found reference to undeclared spring integration channel in service: " + BUG_FILE_PATH +
                        ". Occurrence in file: channel=" + NON_EXISTENT_CHANNEL_ID);

    }

    @Override
    protected BugInstance createBug() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
