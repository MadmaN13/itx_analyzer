package analyzer.detectors.spring.config;

import analyzer.commons.FileUtils;
import analyzer.commons.xml.XmlBugInstanceConstructor;
import analyzer.commons.xml.XmlParser;
import analyzer.commons.xml.XmlTagValue;
import analyzer.detectors.spring.config.commons.AbstractSpringConfigDetector;
import analyzer.detectors.spring.config.commons.BugType;
import analyzer.detectors.spring.config.commons.Constants;
import analyzer.model.xml.XmlChain;
import analyzer.model.xml.XmlConfigFile;
import analyzer.model.xml.XmlJmsMessageDriverChannelAdapter;
import analyzer.model.xml.XmlServiceActivator;
import analyzer.model.xml.generated.BugInstanceType;
import analyzer.model.xml.generated.ObjectFactory;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.List;

/**
 * Created by NM.Rabotaev on 12.05.2017.
 */
public class NonExistentChannelDeclarationDetector extends AbstractSpringConfigDetector {
    private String BUG_FILE_PATH;
    private String NON_EXISTENT_CHANNEL_DECLARATION;
    private String ELEMENT_TYPE;
    public NonExistentChannelDeclarationDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    public void sawBug() {
        List<XmlConfigFile> xmlFiles = getXmlFiles();
        for (XmlConfigFile xmlFile:xmlFiles) {
            if (xmlFile.isSpringIntegrationService()) {
                File xml = xmlFile.getXml();
                BUG_FILE_PATH = xml.getAbsolutePath();
                List<XmlChain> chains = XmlParser.readXmlChains(xml);
                checkChains(chains);
                List<XmlJmsMessageDriverChannelAdapter> adapters = XmlParser.readJmsMessageDrivenChannelAdapters(xml);
                checkAdapters(adapters);
                List<XmlServiceActivator> activators = XmlParser.readXmlServiceActivators(xml);
                checkActivators(activators);
            }
        }

    }

    private void checkChains(List<XmlChain> chains) {
        ELEMENT_TYPE = "Chain";
        for (XmlChain chain:chains) {
            if (chain.getInputChannel().isEmpty()) {
                reportNonExistentInputChannel();
            }
            if (chain.getOutputChannel().isEmpty()) {
                reportNonExistentOutputChannel();
            }
        }
    }

    private void checkAdapters(List<XmlJmsMessageDriverChannelAdapter> adapters) {
        ELEMENT_TYPE = "Jms-message-driven-channel-adapter";
        for (XmlJmsMessageDriverChannelAdapter adapter:adapters) {
            if (adapter.getChannel().isEmpty()) {
                NON_EXISTENT_CHANNEL_DECLARATION = XmlTagValue.SPRING_INTEGRATION_CHANNEL_ELEMENT_REF.getValue();
                reportBug();
            }
        }
    }

    private void checkActivators(List<XmlServiceActivator> activators) {
        ELEMENT_TYPE = "Service-activator";
        for (XmlServiceActivator activator:activators) {
            if (!activator.isChained()) {
                if (activator.getInputChannel().isEmpty()) {
                    reportNonExistentInputChannel();
                }
                if (activator.getOutputChannel().isEmpty()) {
                    reportNonExistentOutputChannel();
                }
            }
        }
    }

    private void reportNonExistentInputChannel() {
        NON_EXISTENT_CHANNEL_DECLARATION = XmlTagValue.SPRING_INTEGRATION_INPUT_CHANNEL_ELEMENT_REF.getValue();
        reportBug();
    }

    private void reportNonExistentOutputChannel() {
        NON_EXISTENT_CHANNEL_DECLARATION = XmlTagValue.SPRING_INTEGRATION_OUTPUT_CHANNEL_ELEMENT_REF.getValue();
        reportBug();
    }

    @Override
    protected void reportBug()  {
        BugInstanceType bugInstance = createConfigBugInstance();
        try {
            String bugXml = getMarshaller().marshal(new ObjectFactory().createConfigBugInstance(bugInstance));
            FileUtils.writeBug(new File(Constants.REPORT_FILE.getValue()), bugXml);
            printMessage("Found absence of necessary spring integration channel declaration in service: " + BUG_FILE_PATH +
                    ".  " + ELEMENT_TYPE  + " needs declaration of " + NON_EXISTENT_CHANNEL_DECLARATION + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected BugInstanceType createConfigBugInstance() {
        return XmlBugInstanceConstructor.bugInstance(
                this.getClass().getName(),
                BugType.NON_EXISTENT_SPRING_INTEGRATION_CHANNEL_DECLARATION.getValue(),
                BUG_FILE_PATH,
                "Found absence of necessary spring integration channel declaration in service: " + BUG_FILE_PATH +
                        ".  " + ELEMENT_TYPE  + " needs declaration of " + NON_EXISTENT_CHANNEL_DECLARATION + ".");
    }

    @Override
    protected BugInstance createBug() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
