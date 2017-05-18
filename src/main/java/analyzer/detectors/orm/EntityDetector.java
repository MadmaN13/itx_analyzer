package analyzer.detectors.orm;

import org.apache.bcel.classfile.AnnotationEntry;

/**
 * Created by NM.Rabotaev on 17.05.2017.
 */
public class EntityDetector {


    private static final String ENTITY_CLASS_NAME = "Ljavax/persistence/Entity;";

    public static boolean isEntity(AnnotationEntry a) {
        return ENTITY_CLASS_NAME.equals(a.getAnnotationType());
    }
}
