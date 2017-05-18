package analyzer.commons;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NM.Rabotaev on 18.05.2017.
 */
public class CollectionsUtilsTest {
    @Test
    public void reduceAnnotationListByClassName() throws Exception {
    }

    @Test
    public void reduceByKey() throws Exception {
        List<?> list = new ArrayList<>();
        Map<String, String> map= new HashMap<>();
        map.put("1","val1_1"); map.put("1","val1_2");
        map.put("2","val2_1"); map.put("2","val2_2"); map.put("2","val2_3");
        map.put("3", "val3_1");
        Map<String, List<String>> stringListMap = CollectionsUtils.reduceMapByKey(map);
        map.entrySet().forEach(System.out::println);
    }

}