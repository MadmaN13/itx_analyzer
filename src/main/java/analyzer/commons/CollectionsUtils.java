package analyzer.commons;

import analyzer.model.ClassedAnnotationEntry;

import java.util.*;

/**
 * Created by NM.Rabotaev on 18.05.2017.
 */
public class CollectionsUtils {

    public static <K,V> Map<K,List<V>> reduceMapByKey(Map<K,V> map) {
        Map<K,List<V>> result = new HashMap<>();
        Set<K> keys = new HashSet<>();
        map.entrySet().forEach(e -> keys.add(e.getKey()));
        List<V> values = new ArrayList<>();
        for (K key:keys) {
            map.entrySet().forEach(e-> {
                if (e.getKey().equals(key)) values.add(e.getValue());
            });
            result.put(key,values);
            values.clear();
        }
        return result;
    }

    public static Map<String,List<ClassedAnnotationEntry>> reduceAnnotationListByClassName(List<ClassedAnnotationEntry> list) {
        Map<String,List<ClassedAnnotationEntry>> result = new HashMap<>();
        Set<String> names = new HashSet<>();
        list.forEach(l -> names.add(l.getClassName()));
        List<ClassedAnnotationEntry> annotations = new ArrayList<>();
        for (String name:names) {
            list.forEach(l -> {
                if (l.getClassName().equals(name)) annotations.add(l);
            });
            result.put(name,annotations);
            annotations.clear();
        }
        return result;
    }
}
