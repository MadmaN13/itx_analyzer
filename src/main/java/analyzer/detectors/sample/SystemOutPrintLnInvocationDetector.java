package analyzer.detectors.sample;

import analyzer.detectors.commons.AbstractBytecodeFileTracingDetector;
import analyzer.detectors.spring.config.commons.BugType;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;

/**
 * Created by nikitarabotaev on 26.04.17.
 */
public class SystemOutPrintLnInvocationDetector extends AbstractBytecodeFileTracingDetector {

    public SystemOutPrintLnInvocationDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    public void sawOpcode(int seen) {
        if (seen == GETSTATIC) {
            FieldDescriptor operand = getFieldDescriptorOperand();
            if (
                    operand.getClassDescriptor().toString().equals("java/lang/System") &&
                    operand.getName().equals("out") &&
                    operand.getSignature().equals("Ljava/io/PrintStream;")) {
                    reportBug();
            }
        }
    }

    protected void reportBug() {
        BugInstance bugInstance = createBug();
        getLogger().printLine("Reporting bug:" + bugInstance);
        getReporter().reportBug(bugInstance);
    }

    protected BugInstance createBug() {
        return new BugInstance(this, BugType.SYSTEM_OUT_PRINTLN_INVOCATION.getValue()
                ,HIGH_PRIORITY)
                .addClassAndMethod(this)
                .addSourceLine(this);
    }
}
