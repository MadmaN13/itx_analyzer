package analyzer.detectors.spring.config.commons;

import analyzer.detectors.commons.AbstractBytecodeFileTracingDetector;
import analyzer.detectors.spring.config.*;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;

import javax.naming.OperationNotSupportedException;

/**
 * Created by NM.Rabotaev on 08.05.2017.
 */
public class ScanSpringConfigClassNameDetector extends AbstractBytecodeFileTracingDetector {

    private final String SPRING_CONFIG_DETECTOR_INVOCATION_TRIGGER_STRING = "ScanSpringConfigClassNameDetector";
    private boolean init_UndeclaredDataSourceUsageDetector = false;
    private boolean init_DifferentSpringConfigFileNameDetector = false;
    private boolean init_NonExistentChannelDeclarationDetector = false;
    private boolean init_NonExistentChannelReferenceDetector = false;
    private boolean init_NonExistentClassReferenceDetector = false;
    private boolean init_NonExistentInContextBeanReferenceDetector = false;

    public ScanSpringConfigClassNameDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    public void sawClass() {
        String className = this.getClassName();
        printMessage("Saw class:" + className);
        if (className.contains(SPRING_CONFIG_DETECTOR_INVOCATION_TRIGGER_STRING)) {
            getLogger().printLine("Starting to analyze spring config...");
            scanForUndeclaredDatasource();
            scanForDifferentSpringConfigFileName();
            scanForNonExistentChannelDeclaration();
            scanForNonExistentChannelReference();
            scanForNonExistentClassReference();
            scanForNonExistentInContextBeanReference();
        };
    }
    private void scanForUndeclaredDatasource() {
        if (!init_UndeclaredDataSourceUsageDetector) {
            UndeclaredDataSourceUsageDetector detector = new UndeclaredDataSourceUsageDetector(null );
            init_UndeclaredDataSourceUsageDetector = true;
            printMessage("Created undeclaredDsDetector: " + detector.hashCode());
            detector.sawBug();
        }
    }


    private void scanForDifferentSpringConfigFileName() {
        if (!init_DifferentSpringConfigFileNameDetector) {
            DifferentSpringConfigFileNameDetector detector = new DifferentSpringConfigFileNameDetector(null);
            init_DifferentSpringConfigFileNameDetector = true;
            printMessage("Created differentSpringConfigFileNameDetector: " + detector.hashCode());
            detector.sawBug();
        }
    }

    private void scanForNonExistentChannelDeclaration() {
        if (!init_NonExistentChannelDeclarationDetector) {
            NonExistentChannelDeclarationDetector detector = new NonExistentChannelDeclarationDetector(null);
            init_NonExistentChannelDeclarationDetector = true;
            printMessage("Created nonExistentChannelDeclarationDetector: " + detector.hashCode());
            detector.sawBug();
        }
    }

    private void scanForNonExistentChannelReference() {
        if (!init_NonExistentChannelReferenceDetector) {
            NonExistentChannelReferenceDetector detector = new NonExistentChannelReferenceDetector(null);
            init_NonExistentChannelReferenceDetector = true;
            printMessage("Created nonExistentChannelReferenceDetector: " + detector.hashCode());
            detector.sawBug();
        }
    }

    private void scanForNonExistentClassReference() {
        if (!init_NonExistentClassReferenceDetector) {
            NonExistentClassReferenceDetector detector = new NonExistentClassReferenceDetector(null);
            init_NonExistentClassReferenceDetector= true;
            printMessage("Created nonExistentClassReferenceDetector: " + detector.hashCode());
            detector.sawBug();
        }
    }

    private void scanForNonExistentInContextBeanReference() {
        if (!init_NonExistentInContextBeanReferenceDetector) {
            NonExistentInContextBeanReferenceDetector detector = new NonExistentInContextBeanReferenceDetector(null);
            init_NonExistentInContextBeanReferenceDetector= true;
            printMessage("Created nonExistentInContextBeanReferenceDetector: " + detector.hashCode());
            detector.sawBug();
        }
    }

    @Override
    protected void reportBug() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }

    @Override
    protected BugInstance createBug() throws OperationNotSupportedException {
        throw new OperationNotSupportedException();
    }
}
