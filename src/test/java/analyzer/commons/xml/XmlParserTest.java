package analyzer.commons.xml;

import analyzer.detectors.spring.config.commons.Constants;
import analyzer.model.xml.*;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * Created by NM.Rabotaev on 10.05.2017.
 */
public class XmlParserTest {

    private static final String servicePath = "C:\\Users\\NM.Rabotaev\\IdeaProjects\\itx_analyzer\\src\\main\\java\\analyzer\\snippets\\spring\\config\\undeclared\\beans\\undeclaredBeanElements.xml";

    @Test
    public void readJmsMessageDrivenChannelAdapters() throws Exception {
        List<XmlJmsMessageDriverChannelAdapter> adapters = XmlParser.readJmsMessageDrivenChannelAdapters(new File(servicePath));
        adapters.forEach(System.out::println);
    }

    @Test
    public void readXmlServiceActivators() throws Exception {
        List<XmlServiceActivator> activators = XmlParser.readXmlServiceActivators(new File(servicePath));
        activators.forEach(System.out::println);
    }


    @Test
    public void readXmlChains() throws Exception {
        List<XmlChain> chains = XmlParser.readXmlChains(new File(servicePath));
        chains.forEach(System.out::println);
    }

    @Test
    public void testRegExp() {
        String anyString = Constants.ANY_STRING.getValue();
        String regExp = anyString + XmlTag.CHAIN_OPEN.getValue()+ anyString;
        String tested = "<serv:chain input-channel=\"inboundChannel\" output-channel=\"outboundChannel2\">";
        System.out.println(tested.matches(regExp));
    }

    /*private static final String globalContextFilePath = "C:\\Users\\NM.Rabotaev\\IdeaProjects\\itx_analyzer\\src\\main\\java\\analyzer\\snippets\\spring\\config\\ds\\global-context.xml";
    private static final String bugJobFilePath = "C:\\Users\\NM.Rabotaev\\IdeaProjects\\itx_analyzer\\src\\main\\java\\analyzer\\snippets\\spring\\config\\ds\\preloadBanks.xml";
    @Test
    public void readXmlBeans() throws Exception {
        List<XmlBean> xmlBeans = XmlParser.readXmlBeans(new File(globalContextFilePath));
        xmlBeans.forEach(System.out::println);
    }

    @Test
    public void readXmlProperties() throws Exception {
        List<XmlProperty> xmlProperties = XmlParser.readXmlProperties(new File(bugJobFilePath));
        xmlProperties.forEach(System.out::println);
    }*/

}