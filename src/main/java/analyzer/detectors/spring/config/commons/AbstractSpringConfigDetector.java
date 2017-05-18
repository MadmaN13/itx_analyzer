package analyzer.detectors.spring.config.commons;

import analyzer.commons.FileUtils;
import analyzer.commons.xml.JAXBMarshaller;
import analyzer.detectors.commons.AbstractBytecodeFileTracingDetector;
import analyzer.model.xml.generated.BugInstanceType;
import analyzer.model.xml.XmlConfigFile;
import edu.umd.cs.findbugs.BugReporter;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by NM.Rabotaev on 08.05.2017.
 */
public abstract class AbstractSpringConfigDetector extends AbstractBytecodeFileTracingDetector {
    private List<XmlConfigFile> xmlFiles;
    private JAXBMarshaller marshaller;

    protected AbstractSpringConfigDetector(BugReporter reporter) {
        super(reporter);
        xmlFiles = FileUtils.findXmlConfigFiles();
        try {
            marshaller = new JAXBMarshaller(BugInstanceType.class);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    protected abstract void sawBug();
    protected abstract BugInstanceType createConfigBugInstance();

    //ACCESSORS
    public List<XmlConfigFile> getXmlFiles() {
        return xmlFiles;
    }
    protected JAXBMarshaller getMarshaller() {
        return marshaller;
    }
}
