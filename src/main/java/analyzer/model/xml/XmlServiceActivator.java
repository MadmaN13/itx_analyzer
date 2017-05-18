package analyzer.model.xml;

/**
 * Created by NM.Rabotaev on 12.05.2017.
 */
public class XmlServiceActivator {

    public XmlServiceActivator() {
        super();
        inputChannel = "";
        outputChannel = "";
        className = "";
        ref = "";
        method = "";
        chained = false;
    }

    private String inputChannel;
    private String outputChannel;
    private String className;
    private String ref;
    private String method;

    private boolean chained;

    //ACCESSORS
    public String getInputChannel() {
        return inputChannel;
    }

    public void setInputChannel(String inputChannel) {
        this.inputChannel = inputChannel;
    }

    public String getOutputChannel() {
        return outputChannel;
    }

    public void setOutputChannel(String outputChannel) {
        this.outputChannel = outputChannel;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public boolean isChained() {
        return chained;
    }

    public void setChained(boolean chained) {
        this.chained = chained;
    }
}
