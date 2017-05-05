package analyzer.detectors;

import analyzer.logger.PrintStreamToFileInterceptor;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.classfile.ClassDescriptor;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nikitarabotaev on 26.04.17.
 */
public class SystemOutPrintLnInvocationDetector extends BytecodeScanningDetector {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemOutPrintLnInvocationDetector.class);
    private PrintStreamToFileInterceptor logger;

    private final BugReporter reporter;

    public SystemOutPrintLnInvocationDetector(BugReporter reporter) {
        this.reporter = reporter;
        this.logger = new PrintStreamToFileInterceptor();
    }

    @Override
    public void sawOpcode(int seen) {
        logger.printLine(this.getClassName()+ " seen opcode: " + seen);
        if (seen == GETSTATIC) {
//            logger.printLine("Found get static!");
            FieldDescriptor operand = getFieldDescriptorOperand();
//            logger.printLine("Operand descriptor: " + operand.toString());
//            logger.printLine("Operand name: " + operand.getName());
//            logger.printLine(operand.getClassDescriptor().toString());
//            logger.printLine(operand.getClass().getCanonicalName());
//            logger.printLine(operand.getSignature());
            if (
                    operand.getClassDescriptor().toString().equals("java/lang/System") &&
                    operand.getName().equals("out") &&
                    operand.getSignature().equals("Ljava/io/PrintStream;")) {
//                logger.printLine("Found sop!");
                reportBug();
            }
        }
    }

    private void reportBug() {
        BugInstance bugInstance = createBug();
        logger.printLine("Reporting bug:" + bugInstance);
        reporter.reportBug(bugInstance);
    }

    private BugInstance createBug() {
        return new BugInstance(this,"SYSTEM_OUT_PRINTLN_INVOCATION",HIGH_PRIORITY)
                .addClassAndMethod(this)
                .addSourceLine(this);
    }
}
