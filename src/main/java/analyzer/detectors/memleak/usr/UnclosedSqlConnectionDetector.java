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
public class UnclosedSqlConnectionDetector extends AbstractBytecodeFileTracingDetector {

    private final static String SQL_CONN_CLASS = "java/sql/Connection";
    private final static String SQL_CONN_CLOSE_METHOD_NAME = "close";


    private MethodDescriptor FOUND_IN_METHOD;
    private int aloadIndex = -1;

    private boolean isMethodLocalVarsInitialized = false;

    List<Connection> connectionsInMethod = new ArrayList<>();
    private final static String SQL_CONNECTION_VAR_SIGNATURE = "Ljava/sql/Connection;";
    private String UNCLOSED_CONN_NAME;

    public UnclosedSqlConnectionDetector(BugReporter reporter) {
        super(reporter);
    }

    @Override
    public void sawOpcode(int seen) {
//        printMessage("In class "+ getClassName()+ " saw opcode: " + seen + ". PC = " + getPC());
        if (!isMethodLocalVarsInitialized) {
//            printMessage(getCode().toString());
            List<LocalVariable> localVariables = Arrays.asList(getCode().getLocalVariableTable().getLocalVariableTable());
            isMethodLocalVarsInitialized = true;
            for (LocalVariable var:localVariables) {
                if (varIsSqlConn(var)) connectionsInMethod.add(new Connection(var));
            }
            printMessage("Connections found in method " + getMethodName());
            connectionsInMethod.forEach(c -> printMessage("Sql connection: " + c.toString()));
        }
        if (seen == ALOAD) {
//            printMessage("Seen ALOAD...");
            aloadIndex = getAloadIndex();
//            printMessage("ALOAD index = " + aloadIndex);
        }
        if (operandIsSqlConnClosed(seen)) {
            for (Connection conn:connectionsInMethod) {
                if (conn.getVarTableIndex() == aloadIndex) conn.setClosed(true);
            }
        }
        if (isLastOpcodeInMethod()) {
            reportUnclosedConnections();
            printMessage("Connections in the end of the method " + getMethodName());
            connectionsInMethod.forEach(c -> printMessage("Sql connection: " + c.toString()));
            connectionsInMethod.clear();
            isMethodLocalVarsInitialized = false;
        }
    }

    private int getAloadIndex() {
        return StringUtils.findAloadIndexInCode(getCode().toString(true), getPC());
    }

    private boolean varIsSqlConn(LocalVariable var) {
        boolean isSqlConn = false;
        if (var.getSignature().equals(SQL_CONNECTION_VAR_SIGNATURE)) isSqlConn = true;
        return isSqlConn;
    }

    private boolean operandIsSqlConnClosed(int seen) {
        boolean isSqlConnClosed = false;
        if (seen == INVOKEINTERFACE) {
            FieldDescriptor operand = getFieldDescriptorOperand();
            if (
                    SQL_CONN_CLASS.equals(operand.getClassDescriptor().toString()) &
                    SQL_CONN_CLOSE_METHOD_NAME.equals(operand.getName())) {
                isSqlConnClosed = true;
                printFieldDescriptorOperand();
                printMessage("Operand is sql conn close!");
            }
        }
        return isSqlConnClosed;
    }

    private void reportUnclosedConnections() {
        FOUND_IN_METHOD = getMethodDescriptor();
        for (Connection connection:connectionsInMethod) {
            if (!connection.isClosed()) {
                UNCLOSED_CONN_NAME = connection.getName();
                reportBug();
            }
        }
    }

    @Override
    protected void reportBug() {
        BugInstance bugInstance = createBug();
        printMessage("Reporting bug: " + bugInstance);
        getReporter().reportBug(bugInstance);
    }

    @Override
    protected BugInstance createBug() {
        return new BugInstance(this, BugType.UNCLOSED_SQL_CONNECTION.getValue(),NORMAL_PRIORITY)
                .addClass(this)
                .addMethod(FOUND_IN_METHOD)
                .addString("Unclosed connection is " + UNCLOSED_CONN_NAME);
    }
}
