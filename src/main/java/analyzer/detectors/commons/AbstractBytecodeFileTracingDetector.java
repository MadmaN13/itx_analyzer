package analyzer.detectors.commons;

import analyzer.logger.SysOutToFileInterceptor;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.BytecodeScanningDetector;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import org.apache.bcel.classfile.*;

import javax.naming.OperationNotSupportedException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by NM.Rabotaev on 08.05.2017.
 */
public abstract class AbstractBytecodeFileTracingDetector extends BytecodeScanningDetector {

    private SysOutToFileInterceptor logger;
    private final BugReporter reporter;
    protected boolean codePrinted;

    protected AbstractBytecodeFileTracingDetector(BugReporter reporter) {
        this.reporter = reporter;
        logger = new SysOutToFileInterceptor();
        codePrinted = false;
    }

    protected abstract void reportBug() throws OperationNotSupportedException;
    protected abstract BugInstance createBug() throws OperationNotSupportedException;

    protected void printOpcode(int seen) {
        printMessage("In class "+ getClassName()+ " saw opcode: " + seen + ". PC = " + getPC());
    }

    protected void printCode(boolean verbose) {
        if (!codePrinted){
        printMessage("-----------------Code---------------------");
        printMessage(getCode().toString(verbose));
        printMessage("-----------------Code end------------------");
        }
    }

    protected void printMethod() {
        printMessage("-----------Method----------------");
        MethodDescriptor md = getMethodDescriptor();
        printMessage("Name: " + md.getName());
        printMessage("Signature: " + md.getSignature());
        printMessage("Slashed class name: " + md.getSlashedClassName());
        printMessage("-----------Method end------------");
    }

    protected void printAnnotationEntry(AnnotationEntry a) {
        printMessage("-----------Annotation----------------");
        printMessage("Annotation type: " + a.getAnnotationType());
        printMessage("Annotation type index: " + a.getAnnotationTypeIndex());
        printMessage("Type index: " + a.getTypeIndex());
        printMessage("Num elementValues: " + a.getNumElementValuePairs());
        List<ElementValuePair> elementValuePairs = Arrays.asList(a.getElementValuePairs());
        for (ElementValuePair pair:elementValuePairs) {
            printMessage("Name string: " + pair.getNameString());
            printMessage("Name index: " + pair.getNameIndex());
            printMessage("Value: " + pair.getValue().stringifyValue());
        }
        printMessage("-----------Annotation end------------");
    }

    protected void printMessage(String message) {
        getLogger().printLine(this.getClass().getName() +" [" + new Date() + "] - " + message);
    }

    protected void printMethodCode() {

    }

    protected void printFieldDescriptor() {
        try {
            FieldDescriptor fd = getFieldDescriptor();
            printMessage("Class descriptor: " + fd.getClassDescriptor().toString());
            printMessage("Operand name: " + fd.getName());
            printMessage("Operand signature: " + fd.getSignature());
        }
        catch (Exception e) {
            printMessage("Field descriptor not available!");
        }
    }

    protected void printFieldDescriptorOperand() {
        try {
        FieldDescriptor operand = getFieldDescriptorOperand();
        printMessage("Class descriptor: " + operand.getClassDescriptor().toString());
        printMessage("Operand name: " + operand.getName());
        printMessage("Operand signature: " + operand.getSignature());
        }
        catch (Exception e) {
            printMessage("Field descriptor operand not available!");
        }
    }

    protected void printField() {
        try {
            Field field = getField();
            printMessage("Field name: " + field.getName());
            printMessage("Field signature: " + field.getSignature());
            printMessage("Field type: " + field.getType().toString());
        }
        catch (Exception e) {
            printMessage("Field not available!");
        }
    }

    protected void printLocalVar(LocalVariable var) {
        printMessage("Index: " + var.getIndex());
        printMessage("Length: " + var.getLength());
        printMessage("Name: " + var.getName());
        printMessage("Name index: " + var.getNameIndex());
        printMessage("Sign: " + var.getSignature());
        printMessage("Sign index: " + var.getSignatureIndex());
        printMessage("Start pc: " + var.getStartPC());
    }

    protected boolean isLastOpcodeInMethod() {
        boolean isLast = false;
        try {
            getNextOpcode();
        }
        catch (ArrayIndexOutOfBoundsException e) {
            printMessage("Last opcode in block: " + getOpcode());
            isLast = true;
        }
        return isLast;
    }
    protected boolean isFirstOpcodeInMethod() {
        boolean isFirst = false;
        try {
            getPrevOpcode(1);
        }
        catch (ArrayIndexOutOfBoundsException e) {
            printMessage("First opcode in block: " + getOpcode());
            isFirst = true;
        }
        return isFirst;
    }

    protected FieldDescriptor s_getFieldDescriptorOperand() {
        FieldDescriptor fd = null;
        try {
            fd = getFieldDescriptorOperand();
        }
        catch (Exception e) {}
        return fd;
    }

    // ACCESSORS
    protected SysOutToFileInterceptor getLogger() {
        return logger;
    }
    public BugReporter getReporter() {
        return reporter;
    }
}
