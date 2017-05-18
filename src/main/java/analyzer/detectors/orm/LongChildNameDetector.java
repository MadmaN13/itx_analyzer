package analyzer.detectors.orm;

import analyzer.commons.CollectionsUtils;
import analyzer.detectors.commons.AbstractBytecodeFileTracingDetector;
import analyzer.detectors.spring.config.commons.BugType;
import analyzer.model.ClassedAnnotationEntry;
import analyzer.model.Entity;
import analyzer.model.VisitedClass;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.AnnotationEntry;
import org.apache.bcel.classfile.Annotations;
import org.apache.bcel.classfile.ElementValuePair;

import java.util.*;

/**
 * Created by NM.Rabotaev on 18.05.2017.
 */
public class LongChildNameDetector extends AbstractBytecodeFileTracingDetector {
    private List<VisitedClass> visitedClasses;
    private boolean annotationsVisited;
    List<ClassedAnnotationEntry> classedAnnotations;

    private static final int DEFAULT_DISCRIMINATOR_COLUMN_LENGTH_NAME = 31;

    private static final String DISCRIMINATOR_COLUMN_ANNOTATION = "Ljavax/persistence/DiscriminatorColumn;";
    private static final String DISCRIMINATOR_COLUMN_LENGTH_NAME = "length";
    private static final String ENTITY_NAME = "name";
    private static final String INHERITANCE_ANNOTATION = "Ljavax/persistence/Inheritance;";
    private static final String INHERITANCE_STRATEGY_NAME = "strategy";
    // TODO: 18.05.2017 find out
    private static final String INHERITANCE_STRATEGY_VALUE = "";


    public LongChildNameDetector(BugReporter reporter) {
        super(reporter);
        annotationsVisited = false;
        visitedClasses = new ArrayList<>();
        classedAnnotations = new ArrayList<>();
    }

    @Override
    public void sawClass() {
        String currentClassName = getClassName();
        printMessage("Saw class: " + currentClassName);
        String superclassName = getSuperclassName();
        for (VisitedClass visited:visitedClasses) {
            if (visited.getName().equals(superclassName)) {
                Entity entity = visited.getEntity();
                if (entity != null && entity.getInheritanceType() == Entity.InheritanceType.SINGLE_TABLE &&
                        entity.getDiscriminatorColumnLength() < currentClassName.length()) {
                    reportBug();
                }
            }
        }
    }

    @Override
    public void visitAnnotation(Annotations annotations) {
        List<AnnotationEntry> entries = Arrays.asList(annotations.getAnnotationEntries());
        for (AnnotationEntry a:entries) {
            String className = getClassName();
            classedAnnotations.add(new ClassedAnnotationEntry(className,a));
            printMessage("Put in list:" + className + "; " + a.toShortString());
        }
        visitedClasses = collectVisitedClasses(classedAnnotations);
    }

    private List<VisitedClass> collectVisitedClasses(List<ClassedAnnotationEntry> classedAnnotations) {
        List<VisitedClass> visitedClasses = new ArrayList<>();
        Map<String, List<ClassedAnnotationEntry>> annoInClasses = CollectionsUtils.reduceAnnotationListByClassName(classedAnnotations);
        for (Map.Entry<String,List<ClassedAnnotationEntry>> entry:annoInClasses.entrySet()) {
            String className = entry.getKey();
            VisitedClass visited = new VisitedClass(className);
            Entity e = null;
            List<ClassedAnnotationEntry> annotations = entry.getValue();
            for (ClassedAnnotationEntry classedAnnotation:annotations) {
                AnnotationEntry a = classedAnnotation.getA();
                if (EntityDetector.isEntity(a)) {
                    e = new Entity(getEntityName(e));
                }
                if (e != null && isDiscriminatorColumn(a)) {
                    e.setDiscriminatorColumnLength(getDiscriminatorColumnLength(a));
                }
                if (e != null && isSingleTableInheritanceType(a)) {
                    e.setInheritanceType(Entity.InheritanceType.SINGLE_TABLE);
                }
            }
            if (e !=null ) visited.setEntity(e);
            visitedClasses.add(visited);
        }
        return visitedClasses;
    }

    private String getEntityName(Entity e) {
        // TODO: 18.05.2017 impl
        return "";
    }

    private boolean isDiscriminatorColumn(AnnotationEntry a) {
        return DISCRIMINATOR_COLUMN_ANNOTATION.equals(a.getAnnotationType());
    }

    private int getDiscriminatorColumnLength(AnnotationEntry a) {
        int length = DEFAULT_DISCRIMINATOR_COLUMN_LENGTH_NAME;
        List<ElementValuePair> params = Arrays.asList(a.getElementValuePairs());
        for (ElementValuePair p:params) {
            if (DISCRIMINATOR_COLUMN_LENGTH_NAME.equals(p.getNameString())) {
                length = Integer.parseInt(p.getValue().stringifyValue());
            }
        }
        return length;
    }

    private boolean isSingleTableInheritanceType(AnnotationEntry a) {
        boolean isSingleTableInheritanceType = false;
        if (INHERITANCE_ANNOTATION.equals(a.getAnnotationType())) {
            List<ElementValuePair> params = Arrays.asList(a.getElementValuePairs());
            for (ElementValuePair p:params) {
                if (
                        INHERITANCE_STRATEGY_NAME.equals(p.getNameString()) &&
                        INHERITANCE_STRATEGY_VALUE.equals(p.getValue().stringifyValue()))
                    isSingleTableInheritanceType = true;
            }
        }
        return isSingleTableInheritanceType;
    }

    @Override
    protected void reportBug() {
        BugInstance bug = createBug();
        printMessage("Reporting bug: " + bug);
        getReporter().reportBug(bug);

    }

    @Override
    protected BugInstance createBug() {
        return new BugInstance(this, BugType.TOO_LONG_SINGLE_TABLE_CHILD_NAME.getValue(), NORMAL_PRIORITY)
                .addClass(this);
    }
}
