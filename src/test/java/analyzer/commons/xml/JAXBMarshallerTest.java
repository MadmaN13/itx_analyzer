package analyzer.commons.xml;

import analyzer.model.xml.generated.BugInstanceType;
import analyzer.model.xml.generated.ObjectFactory;
import org.junit.Test;

import javax.xml.bind.JAXBElement;

/**
 * Created by NM.Rabotaev on 10.05.2017.
 */
public class JAXBMarshallerTest {
    @Test
    public void marshal() throws Exception {

        JAXBMarshaller marshaller = new JAXBMarshaller("analyzer.model.xml.generated");
        BugInstanceType bugInstanceType = XmlBugInstanceConstructor.bugInstance("test", "test", "test", "test");
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<BugInstanceType> configBugInstance = objectFactory.createConfigBugInstance(bugInstanceType);
        String marshal = marshaller.marshal(configBugInstance);
        System.out.println(marshal);
    }

}