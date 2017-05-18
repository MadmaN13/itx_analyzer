package analyzer.model.xml;

/**
 * Created by NM.Rabotaev on 12.05.2017.
 */
public class XmlJmsMessageDriverChannelAdapter {

    private String channel;
    private String destinationName;

    public XmlJmsMessageDriverChannelAdapter() {
        super();
        channel = "";
        destinationName = "";
    }

    //ACCESSORS
    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }
}
