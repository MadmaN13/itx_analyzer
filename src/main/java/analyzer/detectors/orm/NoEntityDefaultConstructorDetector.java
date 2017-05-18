package analyzer.detectors.orm;

import analyzer.detectors.commons.AbstractBytecodeFileTracingDetector;
import analyzer.detectors.spring.config.commons.BugType;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Annotations;
import static analyzer.detectors.orm.EntityDetector.isEntity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by NM.Rabotaev on 17.05.2017.
 */
public class NoEntityDefaultConstructorDetector extends AbstractBytecodeFileTracingDetector {

    private boolean found;
    private final static String INIT_METHOD_NAME = "<init>";
    private final static String DEFAULT_CONSTRUCTOR_SIGN = "()V";

    public NoEntityDefaultConstructorDetector(BugReporter reporter) {
        super(reporter);
        found = false;
    }

    @Override
    public void sawMethod() {
        printMethod();
        if (isDefaultConstructor()) {
            found = true;
            printMessage("Method is def constructor!");
        }
    }

    private boolean isDefaultConstructor() {
        MethodDescriptor md = getMethodDescriptor();
        return INIT_METHOD_NAME.equals(md.getName()) && DEFAULT_CONSTRUCTOR_SIGN.equals(md.getSignature());
    }

    @Override
    public void visitAnnotation(Annotations annotations) {
        List<AnnotationEntry> entries = Arrays.asList(annotations.getAnnotationEntries());
        for (AnnotationEntry a:entries) {
            printAnnotationEntry(a);
            if (isEntity(a) & !found) {
                reportBug();
                found = false;
            }
        }
    }

    @Override
    protected void reportBug() {
        BugInstance bugInstance = createBug();
        printMessage("Reporting bug: " + bugInstance);
        getReporter().reportBug(bugInstance);
    }

    @Override
    protected BugInstance createBug() {
        return new BugInstance(this, BugType.NO_ENTITY_DEFAULT_CONSTRUCTOR.getValue(), HIGH_PRIORITY)
                .addClass(this);
    }
}
