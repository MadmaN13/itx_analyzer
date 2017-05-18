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
public class NonExistentClassReferenceDetector extends AbstractSpringConfigDetector {
    private String BUG_FILE_PATH = "";
    private String REFERENCED_CLASS = "";
    private String BEAN_ID = "";
    private List<String> javaFiles;


    public NonExistentClassReferenceDetector(BugReporter reporter) {
        super(reporter);
        javaFiles = FileUtils.findJavaFiles();
    }

    @Override
    public void sawBug() {
        List<XmlConfigFile> xmlFiles = getXmlFiles();
        for (XmlConfigFile xmlFile: xmlFiles) {
            List<XmlBean> beans = XmlParser.readXmlBeans(xmlFile.getXml());
            for (XmlBean bean:beans) {
                boolean isFound = false;
                String className = bean.getClassName();
                if (className != null && !className.isEmpty()) {
                    String slashedClassName = className.replaceAll("\\.", "\\\\") + Constants.JAVA_FILE_FLAG.getValue();
                    for (String javaFile:javaFiles) {
                        if (javaFile.endsWith(slashedClassName)) isFound = true;
                    }
                    if (!isFound) {
                        BUG_FILE_PATH = xmlFile.getXml().getAbsolutePath();
                        REFERENCED_CLASS = className;
                        BEAN_ID = bean.getId();
                        reportBug();
                    }
                }
            }
        }
    }

    @Override
    protected void reportBug()  {
        BugInstanceType bugInstance = createConfigBugInstance();
        try {
            String bugXml = getMarshaller().marshal(new ObjectFactory().createConfigBugInstance(bugInstance));
            FileUtils.writeBug(new File(Constants.REPORT_FILE.getValue()), bugXml);
            printMessage("Found reference to non existent source file from bean class tag in file: " + BUG_FILE_PATH +
                    ". Bean id: " + BEAN_ID + ". Referenced class: " + REFERENCED_CLASS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BugInstanceType createConfigBugInstance() {
        return XmlBugInstanceConstructor.bugInstance(this.getClass().getName(),
                BugType.NON_EXISTENT_CLASS_REFERENCE.getValue(),
                BUG_FILE_PATH,
                "Found reference to non existent source file from bean class tag in file: " + BUG_FILE_PATH +
        ". Bean id: " + BEAN_ID + ". Referenced class: " + REFERENCED_CLASS);
    }

    @Override
    protected BugInstance createBug() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
