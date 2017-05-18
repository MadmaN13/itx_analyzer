package analyzer.detectors.orm;

import analyzer.commons.StringUtils;
import analyzer.detectors.commons.AbstractBytecodeFileTracingDetector;
import analyzer.detectors.spring.config.commons.BugType;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;

/**
 * Created by NM.Rabotaev on 17.05.2017.
 */
public class HqlParamsUnsetDetector extends AbstractBytecodeFileTracingDetector {
    private MethodDescriptor foundInMethod;

    private String ldc_string;
    private String ldc_query;
    private boolean paramSet;
    private boolean isParametrizedQuery;

    private final static String EM_CLASS = "javax/persistence/EntityManager";
    private final static String CREATE_QUERY_METHOD = "createQuery";
    private final static String QUERY_CLASS = "javax/persistence/TypedQuery";
    private final static String SET_PARAM_METHOD = "setParameter";
    private final static String GET_RESULT_LIST_METHOD = "getResultList";
    private final static String GET_SINGLE_RESULT_METHOD = "getSingleResult";
    private final static String GET_FIRST_RESULT_METHOD = "getFirstResult";

    public HqlParamsUnsetDetector(BugReporter reporter) {
        super(reporter);
        ldc_string = "";
        ldc_query = "";
        paramSet = false;
        isParametrizedQuery = false;
    }

    @Override
    public void sawOpcode(int seen) {
//        printCode(true); codePrinted = true;
//        printOpcode(seen);
        if (seen == LDC) {
//            printMessage("Seen LDC!");
            if (StringUtils.ldcParamIsString(getCode().toString(true), getPC())) {
                ldc_string = StringUtils.getLdcStringValueInCode(getCode().toString(true), getPC());
            }
//            printMessage("Loaded constant: " + ldc_string);
        }
        if (isCreateQuery(seen)) {
            ldc_query = ldc_string;
//            printMessage("Created query: " + ldc_query);
            isParametrizedQuery = StringUtils.queryIsParametrized(ldc_query);
//            printMessage("isParametrizedQuery: " + isParametrizedQuery);
        }
        if (isSetParameter(seen)) {
//            printMessage("Param set!");
            paramSet = true;
        }
        if (isGetResult(seen) && isParametrizedQuery && !paramSet) {
            foundInMethod = getMethodDescriptor();
            reportBug();
            resetConstants();
        }
        if (isLastOpcodeInMethod()) {
            resetConstants();
            codePrinted = false;
        }
    }

    private void resetConstants() {
        ldc_query = "";
        paramSet = false;
        isParametrizedQuery = false;
    }

    private boolean isCreateQuery(int seen) {
        FieldDescriptor op = s_getFieldDescriptorOperand();
        return op!=null && seen == INVOKEINTERFACE &&
                EM_CLASS.equals(op.getClassDescriptor().toString()) &&
                CREATE_QUERY_METHOD.equals(op.getName());
    }

    private boolean isSetParameter(int seen) {
        FieldDescriptor op = s_getFieldDescriptorOperand();
        return op!=null && seen == INVOKEINTERFACE &&
                QUERY_CLASS.equals(op.getClassDescriptor().toString()) &&
                SET_PARAM_METHOD.equals(op.getName());

    }

    private boolean isGetResult(int seen) {
        FieldDescriptor op = s_getFieldDescriptorOperand();
        boolean isGetResult = false;
        if (op!= null) {
            String name = op.getName();
            isGetResult = seen == INVOKEINTERFACE &&
                QUERY_CLASS.equals(op.getClassDescriptor().toString()) &&
                (GET_FIRST_RESULT_METHOD.equals(name) ||
                        GET_RESULT_LIST_METHOD.equals(name) ||
                        GET_SINGLE_RESULT_METHOD.equals(name));
        }
        return isGetResult;
    }

    @Override
    protected void reportBug() {
        BugInstance bug = createBug();
        printMessage("Reporting bug: " + bug);
        getReporter().reportBug(bug);
    }

    @Override
    protected BugInstance createBug() {
        return new BugInstance(this, BugType.HQL_PARAMS_UNSET.getValue(), HIGH_PRIORITY)
                .addClass(this)
                .addMethod(foundInMethod);
    }
}
