package analyzer.detectors.memleak.usr;

import analyzer.detectors.commons.AbstractBytecodeFileTracingDetector;
import analyzer.detectors.spring.config.commons.BugType;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;

/**
 * Created by NM.Rabotaev on 12.05.2017.
 */
public class UnclosedEntityManagerDetector extends AbstractBytecodeFileTracingDetector {

    private static final String EM_CLASS_DESCRIPTOR = "javax/persistence/EntityManager";
    private static final String CLOSE_METHOD_NAME= "close";
    private static final String EMF_CLASS_DESCRIPTOR = "javax/persistence/EntityManagerFactory";
    private static final String CREATE_EM_METHOD_NAME = "createEntityManager";
    private static final String CREATE_EM_SIGN = "()Ljavax/persistence/EntityManager;";
    private MethodDescriptor FOUND_IN_METHOD;
    private int EM_CREATED_IN_METHOD = 0;
    private int EM_CLOSED_IN_METHOD = 0;

    @Override
    public void sawOpcode(int seen) {
//        printMessage("In class "+ getClassName()+ " saw opcode: " + seen + ". PC = " + getPC());
        if (operandIsEmOpen(seen)) EM_CREATED_IN_METHOD ++;
        if (operandIsEmClosed(seen)) EM_CLOSED_IN_METHOD ++;
        if (isLastOpcodeInMethod()) {
            if (!allEmInMethodAreClosed()) {
                FOUND_IN_METHOD = getMethodDescriptor();
                reportBug();
            }
            resetCounters();
        }
    }

    private boolean operandIsEmOpen(int seen) {
        boolean isEmOpen = false;
        if (seen == INVOKEINTERFACE) {
            FieldDescriptor operand = getFieldDescriptorOperand();
            if (
                    EMF_CLASS_DESCRIPTOR.equals(operand.getClassDescriptor().toString()) &
                    CREATE_EM_METHOD_NAME.equals(operand.getName()) &
                    CREATE_EM_SIGN.equals(operand.getSignature())) {
                isEmOpen = true;
                printMessage("Found em open: " + operand);
            }
        }
        return isEmOpen;
    }

    private boolean operandIsEmClosed(int seen) {
        boolean isEmClosed = false;
        if (seen == INVOKEINTERFACE) {
            FieldDescriptor operand = getFieldDescriptorOperand();
            if (
                    EM_CLASS_DESCRIPTOR.equals(operand.getClassDescriptor().toString()) &
                    CLOSE_METHOD_NAME.equals(operand.getName())) {
                isEmClosed = true;
                printMessage("Found em close: " + operand);
            }

        }
        return isEmClosed;
    }

    private boolean allEmInMethodAreClosed() {
        boolean allEmAreClosed = false;
        printMessage("Em open in method: " + EM_CREATED_IN_METHOD);
        printMessage("Em closed in method: " + EM_CLOSED_IN_METHOD);
        // TODO: 13.05.2017 implement different cases: Too many em closed, all em are closed, not all em are closed
        if (EM_CREATED_IN_METHOD <= EM_CLOSED_IN_METHOD) allEmAreClosed = true;
        return allEmAreClosed;
    }

    private void resetCounters() {
        EM_CREATED_IN_METHOD = 0;
        EM_CLOSED_IN_METHOD = 0;
    }

    public UnclosedEntityManagerDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    protected void reportBug() {
        BugInstance bugInstance = createBug();
        printMessage("Reporting bug: " + bugInstance);
        getReporter().reportBug(bugInstance);

    }

    @Override
    protected BugInstance createBug() {
        return new BugInstance(this, BugType.UNCLOSED_ENTITY_MANAGER.getValue(),
        NORMAL_PRIORITY)
                .addClass(this)
                .addMethod(FOUND_IN_METHOD);
    }
}
