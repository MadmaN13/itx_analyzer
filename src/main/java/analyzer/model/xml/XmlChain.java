package analyzer.model.xml;

/**
 * Created by NM.Rabotaev on 12.05.2017.
 */
public class XmlChain {

    public XmlChain() {
        super();
        inputChannel = "";
        outputChannel = "";
    }

    private String inputChannel;
    private String outputChannel;

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
}
