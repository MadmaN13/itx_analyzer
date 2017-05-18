package analyzer.commons;

import org.junit.Test;

/**
 * Created by NM.Rabotaev on 17.05.2017.
 */
public class StringUtilsTest {
    @Test
    public void ldcParamIsString() throws Exception {
        String code = "18:   ldc\t\t\"select from Test\" (8)\n" +
                "20:   astore\t\t%4\n" +
                "22:   aload_2\n" +
                "23:   ldc\t\t\"select p from BasePayment p where p.rqUid = :rquid\" (2)\n" +
                "25:   ldc\t\tsu.jet.cib.af.skbbank.facts.payments.BasePayment (10)\n" +
                "27:   invokeinterface\tjavax.persistence.EntityManager.createQuery (Ljava/lang/String;Ljava/lang/Class;)Ljavax/persistence/TypedQuery; (11)\t3\t0\n" +
                "32:   ldc\t\t\"rquid\" (12)\n" +
                "34:   aload_1";
        System.out.println(StringUtils.ldcParamIsString(code,23));
        System.out.println(StringUtils.ldcParamIsString(code,25));
        System.out.println(StringUtils.ldcParamIsString(code,32));
    }

    @Test
    public void getLdcStringValueInCode() throws Exception {
        String code = "ldc\t\t\"select from Test\" (8)\n" +
                "20:   astore\t\t%4\n" +
                "22:   aload_2\n" +
                "23:   ldc\t\t\"select p from BasePayment p where p.rqUid = :rquid\" (2)\n" +
                "25:   ldc\t\tsu.jet.cib.af.skbbank.facts.payments.BasePayment (10)\n" +
                "27:   invokeinterface\tjavax.persistence.EntityManager.createQuery (Ljava/lang/String;Ljava/lang/Class;)Ljavax/persistence/TypedQuery; (11)\t3\t0\n" +
                "32:   ldc\t\t\"rquid\" (12)\n" +
                "34:   aload_1";
        System.out.println(StringUtils.getLdcStringValueInCode(code, 23));
    }

    @Test
    public void queryIsParameterized() throws Exception {
        System.out.println(StringUtils.queryIsParametrized("select p from BasePayment p where p.rqUid = :rquid and p.value = :value"));
        System.out.println(StringUtils.queryIsParametrized(""));
        System.out.println(StringUtils.queryIsParametrized("select t from Test t"));
    }

}