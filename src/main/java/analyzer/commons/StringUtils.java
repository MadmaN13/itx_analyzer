package analyzer.commons;

import analyzer.detectors.spring.config.commons.Constants;
import org.apache.bcel.classfile.Code;

import java.util.Arrays;
import java.util.List;

/**
 * Created by NM.Rabotaev on 17.05.2017.
 */
public class StringUtils {

    public static boolean queryIsParametrized(String query) {
        return !query.isEmpty() && query.split(":").length >= 2;
    }

    public static int findAloadIndexInCode(String code, int pc) {
        int index = -1;
        List<String> lines = Arrays.asList(code.split("\n"));
        String anyString = Constants.ANY_STRING.getValue();
        String regex = String.valueOf(pc) + ":" + anyString + "aload" + anyString;
        String notIndexedAloadSplitVal = "%";
        String indexedAloadSplitVal = "_";
        for (String line:lines) {
            if (line.matches(regex)) {
                if (line.contains(notIndexedAloadSplitVal)) index =  Integer.valueOf(Arrays.asList(line.split(notIndexedAloadSplitVal)).get(1));
                if (line.contains(indexedAloadSplitVal)) index =  Integer.valueOf(Arrays.asList(line.split(indexedAloadSplitVal)).get(1));

            }
        }
        return index;
    }

    public static String getLdcStringValueInCode(String code, int pc) {
        String ldcStringValue = "";
        List<String> lines = Arrays.asList(code.split("\n"));
        String anyString = Constants.ANY_STRING.getValue();
        String regex = String.valueOf(pc) + ":" + anyString + "ldc" + anyString;
        for (String line:lines) {
            if (line.matches(regex)) {
                String ldcValue = line.split("ldc")[1];
                if (isStringValue(ldcValue)) {
                    String[] split = ldcValue.split("\"");
                    if (split.length == 3) {
                        ldcStringValue = split[1];
                        break;
                    }
                }
            }
        }
        return ldcStringValue;
    }

    private static boolean isStringValue(String ldcValue) {
        return ldcValue.matches(".*\".*\".*");
    }

    public static boolean ldcParamIsString(String code, int pc) {
        boolean isString = false;
        List<String> lines = Arrays.asList(code.split("\n"));
        String anyString = Constants.ANY_STRING.getValue();
        String regex = String.valueOf(pc) + ":" + anyString + "ldc" + anyString;
        for (String line:lines) {
            if (line.matches(regex)) {
                String ldcValue = line.split("ldc")[1];
                isString =  isStringValue(ldcValue);
            }
        }
        return isString;
    }
}
