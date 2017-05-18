package analyzer.model;

import org.apache.bcel.classfile.LocalVariable;

/**
 * Created by NM.Rabotaev on 13.05.2017.
 */
public class Connection {

    private int varTableIndex;
    private String name;
    boolean isClosed;

    public Connection() {
        super();
        varTableIndex = -1;
        name = "";
        isClosed = false;
    }

    @Override
    public String toString() {
        return new StringBuilder().append("Index = ").append(varTableIndex).append("; Name = ").append(name).append("; isClosed = ").append(isClosed).toString();
    }

    public Connection(LocalVariable var) {
        super();
        varTableIndex = var.getIndex();
        name = var.getName();
        isClosed = false;


    }

    public int getVarTableIndex() {
        return varTableIndex;
    }

    public void setVarTableIndex(int varTableIndex) {
        this.varTableIndex = varTableIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
