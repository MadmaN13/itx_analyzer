package analyzer.model;

import org.apache.bcel.classfile.AnnotationEntry;

/**
 * Created by NM.Rabotaev on 18.05.2017.
 */
public class ClassedAnnotationEntry {

    private String className;
    private AnnotationEntry a;

    public ClassedAnnotationEntry(String className, AnnotationEntry a) {
        this.className = className;
        this.a = a;
    }

    public String getClassName() {
        return className;
    }

    public AnnotationEntry getA() {
        return a;
    }
}
