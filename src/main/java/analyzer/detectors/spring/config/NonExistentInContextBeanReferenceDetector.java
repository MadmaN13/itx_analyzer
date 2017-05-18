package analyzer.detectors.spring.config;

import analyzer.commons.FileUtils;
import analyzer.commons.xml.XmlBugInstanceConstructor;
import analyzer.commons.xml.XmlParser;
import analyzer.detectors.spring.config.commons.AbstractSpringConfigDetector;
import analyzer.detectors.spring.config.commons.BugType;
import analyzer.detectors.spring.config.commons.Constants;
import analyzer.model.xml.XmlBean;
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
public class NonExistentInContextBeanReferenceDetector extends AbstractSpringConfigDetector{
    private String BUG_FILE_PATH = "";
    private String NON_EXISTENT_BEAN_ID = "";


    public NonExistentInContextBeanReferenceDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    public void sawBug() {
        List<XmlConfigFile> xmlFiles = getXmlFiles();
        for (XmlConfigFile xmlFile:xmlFiles) {
            if (xmlFile.isSpringConfig()) {
                List<XmlBean> beans = XmlParser.readXmlBeans(xmlFile.getXml());
                List<String> refs = XmlParser.readReferences(xmlFile.getXml());
                for (String ref:refs) {
                    boolean beanPresent = false;
                    for (XmlBean bean:beans) {
                        String beanId = bean.getId();
                        if (ref.equals(beanId)) beanPresent = true;
                    }
                    if (!beanPresent) {
                        BUG_FILE_PATH = xmlFile.getXml().getAbsolutePath();
                        NON_EXISTENT_BEAN_ID = ref;
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
            printMessage("Found reference to undeclared bean in spring config file context: " + BUG_FILE_PATH +
            ". Occurrence in file: ref=" + NON_EXISTENT_BEAN_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected BugInstanceType createConfigBugInstance() {
        return XmlBugInstanceConstructor.bugInstance(this.getClass().getName(),
                BugType.NON_EXISTENT_IN_CONTEXT_BEAN_REFERENCE.getValue(),
                BUG_FILE_PATH,
                "Found reference to undeclared bean in spring config file context: " + BUG_FILE_PATH +
                        ". Occurrence in file: ref=" + NON_EXISTENT_BEAN_ID);
    }

    @Override
    protected BugInstance createBug() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
