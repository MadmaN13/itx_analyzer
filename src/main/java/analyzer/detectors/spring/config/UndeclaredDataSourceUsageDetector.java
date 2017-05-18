package analyzer.detectors.spring.config;

import analyzer.commons.FileUtils;
import analyzer.commons.xml.XmlBugInstanceConstructor;
import analyzer.commons.xml.XmlParser;
import analyzer.commons.xml.XmlTagValue;
import analyzer.detectors.spring.config.commons.AbstractSpringConfigDetector;
import analyzer.detectors.spring.config.commons.BugType;
import analyzer.detectors.spring.config.commons.Constants;
import analyzer.model.xml.*;
import analyzer.model.xml.generated.BugInstanceType;
import analyzer.model.xml.generated.ObjectFactory;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;

import javax.naming.OperationNotSupportedException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NM.Rabotaev on 08.05.2017.
 */
public class UndeclaredDataSourceUsageDetector extends AbstractSpringConfigDetector {
    private String UNDECLARED_DS_IN_USE_NAME = "";
    private String BUG_FILE_PATH = "";

    public UndeclaredDataSourceUsageDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    public void sawBug() {
            List<XmlConfigFile> xmlFiles = getXmlFiles();
            printMessage("Xml files found: " + xmlFiles);
            File globalContextFile = getGlobalContextFile(xmlFiles);
            List<String> declaredDs = readDsNames(globalContextFile);
            printMessage("Declared ds found: " + declaredDs);
            for (XmlConfigFile xmlFile:xmlFiles) {
                if (xmlFile.isSpringConfig()) {
                    List<XmlProperty> dsInFile = readPropertyDsRefs(xmlFile.getXml());
                    for (XmlProperty ds:dsInFile) {
                        if (!declaredDs.contains(ds.getRef())) {
                            UNDECLARED_DS_IN_USE_NAME = ds.getRef();
                            BUG_FILE_PATH = xmlFile.getXml().getAbsolutePath();
                            printMessage("Found usage of undeclared ds: " + UNDECLARED_DS_IN_USE_NAME + " in file: " + BUG_FILE_PATH);
                            reportBug();
                        }
                    }

                }

            }
    }

    private List<XmlProperty> readPropertyDsRefs(File xml) {
        List<XmlProperty> dsProperties = new ArrayList<>();
        List<XmlProperty> properties = XmlParser.readXmlProperties(xml);
        for (XmlProperty property:properties) {
            if (property.getName().equals(XmlTagValue.DATASOURCE_NAME.getValue())) dsProperties.add(property);
        }
        return dsProperties;
    }

    private File getGlobalContextFile(List<XmlConfigFile> xmlFiles) {
        File globalContextFile = null;
        for (XmlConfigFile xmlFile: xmlFiles) {
            File file = xmlFile.getXml();
            if (file.getName().contains(Constants.GLOBAL_CONTEXT_FILE_NAME.getValue())) globalContextFile = file;
        }
        return globalContextFile;
    }

    private List<String> readDsNames(File globalContextFile) {
        List<String> dsNames = new ArrayList<>();
        if (globalContextFile != null) {
            List<XmlBean> beans = XmlParser.readXmlBeans(globalContextFile);
            for (XmlBean bean:beans) {
                if (bean.getClassName().equals(XmlTagValue.DATASOURCE_CLASS_NAME.getValue())) dsNames.add(bean.getId());
            }
        }
        return dsNames;
    }

    protected void reportBug() {
        BugInstanceType bugInstance = createConfigBugInstance();
        try {
            String bugXml = getMarshaller().marshal(new ObjectFactory().createConfigBugInstance(bugInstance));
            FileUtils.writeBug(new File(Constants.REPORT_FILE.getValue()), bugXml);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected BugInstanceType createConfigBugInstance() {
        return XmlBugInstanceConstructor.bugInstance(
                this.getClass().getName(),
                BugType.UNDECLARED_DATA_SOURCE_USAGE.getValue(),
                BUG_FILE_PATH,
                "Found usage of undeclared datasource with name: " + UNDECLARED_DS_IN_USE_NAME);
    }

    protected BugInstance createBug() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
