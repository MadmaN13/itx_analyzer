package analyzer.commons;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by NM.Rabotaev on 13.05.2017.
 */
public class FileUtilsTest {
    @Test
    public void findAloadIndexInCode() throws Exception {
        String code = "37:   aconst_null\n" +
                "38:   astore\t\t%8\n" +
                "40:   aload\t\t%7\n" +
                "42:   iconst_1\n" +
                "43:   aload_1\n" +
                "44:   invokeinterface\tjava.sql.PreparedStatement.setString (ILjava/lang/String;)V (5)\t3\t0\n" +
                "49:   aload\t\t%7\n" +
                "51:   invokeinterface\tjava.sql.PreparedStatement.executeQuery ()Ljava/sql/ResultSet; (6)\t1\t0";
        int aloadIndexInCode = StringUtils.findAloadIndexInCode(code, 43);
        System.out.println(aloadIndexInCode);
    }

}