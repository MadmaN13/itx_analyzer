package analyzer.detectors.memleak.usr;

import analyzer.commons.FileUtils;
import analyzer.commons.StringUtils;
import analyzer.detectors.commons.AbstractBytecodeFileTracingDetector;
import analyzer.detectors.spring.config.commons.BugType;
import analyzer.model.Connection;
import edu.umd.cs.findbugs.BugInstance;
import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.classfile.FieldDescriptor;
import edu.umd.cs.findbugs.classfile.MethodDescriptor;
import org.apache.bcel.classfile.LocalVariable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by NM.Rabotaev on 13.05.2017.
 */
public class UnclosedUrlConnectionDetector extends AbstractBytecodeFileTracingDetector {

    private final static String URL_CONN_CLASS = "javax/net/ssl/HttpsURLConnection";
    private final static String URL_CONN_DISCONNECT_METHOD_NAME = "disconnect";


    private MethodDescriptor foundInMethod;
    private int aloadIndex = -1;

    private boolean isMethodLocalVarsInitialized = false;

    List<Connection> connectionsInMethod = new ArrayList<>();
    private final static String URL_CONNECTION_VAR_SIGNATURE = "Ljavax/net/ssl/HttpsURLConnection;";
    private String UNCLOSED_CONN_NAME;

    public UnclosedUrlConnectionDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    public void sawOpcode(int seen) {
        printOpcode(seen);
        if (!isMethodLocalVarsInitialized) {
//            printMessage(getCode().toString());
            List<LocalVariable> localVariables = Arrays.asList(getCode().getLocalVariableTable().getLocalVariableTable());
            isMethodLocalVarsInitialized = true;
            for (LocalVariable var:localVariables) {
                if (varIsUrlConn(var)) connectionsInMethod.add(new Connection(var));
            }
            printMessage("Connections found in method " + getMethodName());
            connectionsInMethod.forEach(c -> printMessage("Url connection: " + c.toString()));
        }
        if (isAload(seen)) {
            aloadIndex = getAloadIndex();
//            printMessage("Seen ALOAD: " + seen + "; index = " + aloadIndex);
        }
        if (operandIsUrlConnDisconnect(seen)) {
            for (Connection conn:connectionsInMethod) {
                if (conn.getVarTableIndex() == aloadIndex) conn.setClosed(true);
            }
        }
        if (isLastOpcodeInMethod()) {
            reportUnclosedConnections();
            printMessage("Connections in the end of the method " + getMethodName());
            connectionsInMethod.forEach(c -> printMessage("Url connection: " + c.toString()));
            connectionsInMethod.clear();
            isMethodLocalVarsInitialized = false;
        }
    }

    private boolean varIsUrlConn(LocalVariable var) {
        boolean isUrlConn = false;
        if (var.getSignature().equals(URL_CONNECTION_VAR_SIGNATURE)) isUrlConn = true;
        return isUrlConn;
    }


    private boolean isAload(int seen) {
        return  (seen == ALOAD || seen == ALOAD_0 || seen == ALOAD_1 || seen == ALOAD_2 || seen == ALOAD_3);
    }

    private int getAloadIndex() {
        return StringUtils.findAloadIndexInCode(getCode().toString(true), getPC());
    }

    private boolean operandIsUrlConnDisconnect(int seen) {
        boolean isDisconnect = false;
        if (seen == INVOKEVIRTUAL) {
            FieldDescriptor operand = getFieldDescriptorOperand();
            printFieldDescriptorOperand();
            if (
                            URL_CONN_CLASS.equals(operand.getClassDescriptor().toString()) &
                            URL_CONN_DISCONNECT_METHOD_NAME.equals(operand.getName())
                    ) {
                isDisconnect = true;
                printMessage("Operand is url conn disconnect!");
            }
        }
        return  isDisconnect;
    }

    private void reportUnclosedConnections() {
        foundInMethod = getMethodDescriptor();
        for (Connection connection:connectionsInMethod) {
            if (!connection.isClosed()) {
                UNCLOSED_CONN_NAME = connection.getName();
                reportBug();
            }
        }
    }

    @Override
    protected void reportBug()  {
        BugInstance bugInstance = createBug();
        printMessage("Reporting bug: " + bugInstance);
        getReporter().reportBug(bugInstance);

    }

    @Override
    protected BugInstance createBug() {
        return new BugInstance(this, BugType.UNCLOSED_URL_CONNECTION.getValue(),NORMAL_PRIORITY)
                .addClass(this)
                .addMethod(foundInMethod)
                .addString("Unclosed connection is " + UNCLOSED_CONN_NAME);
    }
}
