package analyzer.detectors.orm;

import analyzer.detectors.commons.AbstractBytecodeFileTracingDetector;
import analyzer.detectors.spring.config.commons.BugType;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Annotations;
import org.apache.bcel.classfile.ElementValuePair;

import java.util.Arrays;
import java.util.List;

/**
 * Created by NM.Rabotaev on 18.05.2017.
 */
public class ManyEagerFieldsDetector extends AbstractBytecodeFileTracingDetector {

    private int eagerFieldsNum;
    private String className;
    private boolean reported;
    private final static int EAGER_FIELDS_THRESHOLD = 5;
    private final static String ONE_TO_ONE_ANNOTATION = "Ljavax/persistence/OneToOne;";
    private final static String ONE_TO_MANY_ANNOTATION = "Ljavax/persistence/OneToMany;";
    private final static String MANY_TO_ONE_ANNOTATION = "Ljavax/persistence/ManyToOne;";
    private final static String FETCH_NAME = "fetch";
    private final static String FETCH_EAGER_VALUE = "EAGER";

    public ManyEagerFieldsDetector(BugReporter reporter) {
        super(reporter);
        eagerFieldsNum = 0;
        className = "";
        reported = false;
    }


    @Override
    public void visitAnnotation(Annotations annotations) {
        String currentClassName = getClassName();
//        printMessage("Visiting class:" + currentClassName);
        if (!className.equals(currentClassName)) {
            className = currentClassName;
//            printMessage("Reset eagerFieldNum from: " + eagerFieldsNum);
            eagerFieldsNum = 0;
            reported = false;
        }
        List<AnnotationEntry> entries = Arrays.asList(annotations.getAnnotationEntries());
        for (AnnotationEntry a:entries) {
//            printAnnotationEntry(a);
            if (isAssociation(a) && isEagerField(a)) {
                eagerFieldsNum++;
                printMessage("Found eager field! Eager fields num: " + eagerFieldsNum);
                if (eagerFieldsNum >= EAGER_FIELDS_THRESHOLD && !reported) {
                        reportBug();
                        reported = true;
                }
            }
        }
    }



    private boolean isAssociation(AnnotationEntry a) {
        String type = a.getAnnotationType();
        return  ONE_TO_MANY_ANNOTATION.equals(type) ||
                ONE_TO_ONE_ANNOTATION.equals(type) ||
                MANY_TO_ONE_ANNOTATION.equals(type);
    }

    private boolean isEagerField(AnnotationEntry a) {
        boolean isEager = false;
        List<ElementValuePair> params = Arrays.asList(a.getElementValuePairs());
        for (ElementValuePair p:params) {
            if (FETCH_NAME.equals(p.getNameString()) && FETCH_EAGER_VALUE.equals(p.getValue().stringifyValue())) {
                isEager = true;
                break;
            }
        }
        return isEager;
    }

    @Override
    protected void reportBug() {
        BugInstance bug = createBug();
        printMessage("Reporting bug: " + bug);
        getReporter().reportBug(bug);
    }

    @Override
    protected BugInstance createBug() {
        return new BugInstance(this, BugType.TOO_MANY_EAGER_FIELDS.getValue(), NORMAL_PRIORITY)
                .addClass(this);
    }
}
