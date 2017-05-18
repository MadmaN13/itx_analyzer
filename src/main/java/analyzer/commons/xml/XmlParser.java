package analyzer.commons.xml;

import analyzer.detectors.spring.config.commons.Constants;
import analyzer.model.xml.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created by NM.Rabotaev on 10.05.2017.
 */
public class XmlParser {

    public static List<String> readDeclaredChannels(File xml) {
        return getAllTagParamValues(XmlTagValue.SPRING_INTEGRATION_CHANNEL_ELEMENT_ID.getValue(), xml);
    }

    public static Set<String> readReferencedChannels(File xml) {
        Set<String> referencedChannels = new HashSet<>();
        getAllTagParamValues(XmlTagValue.SPRING_INTEGRATION_CHANNEL_ELEMENT_REF.getValue(), xml).forEach(v->referencedChannels.add(v));
        return  referencedChannels;
    }

    public static List<String> readReferences(File xml) {
        List<String> refs = new ArrayList<>();
        String tagRefValue = XmlTagValue.PROPERTY_REF.getValue();
        try (Scanner scanner = new Scanner(xml)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(tagRefValue+"=")) {
                    String ref = getAllTagParamValues(tagRefValue, line).get(0);
                    refs.add(ref);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return refs;


    }

    public static List<XmlBean> readXmlBeans(File xml) {
        List<XmlBean> beans = new ArrayList<>();
        try (Scanner scanner = new Scanner(xml)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(XmlTag.BEAN_OPEN.getValue())) {
                    XmlBean bean = getXmlBean(line,scanner);
                    beans.add(bean);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return beans;
    }

    public static List<XmlProperty> readXmlProperties(File xml) {
        List<XmlProperty> properties = new ArrayList<>();
        try (Scanner scanner = new Scanner(xml)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(XmlTag.PROPERTY_OPEN.getValue())) {
                    XmlProperty property = getXmlProperty(line, scanner);
                    properties.add(property);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static List<XmlChain> readXmlChains(File xml) {
        String anyString = Constants.ANY_STRING.getValue();
        List<XmlChain> chains = new ArrayList<>();
        try (Scanner scanner = new Scanner(xml)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.matches(anyString + XmlTag.CHAIN_OPEN.getValue() + anyString)) {
                    XmlChain chain = getXmlChain(line, scanner);
                    chains.add(chain);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return chains;
    }

    public static List<XmlJmsMessageDriverChannelAdapter> readJmsMessageDrivenChannelAdapters(File xml) {
        String anyString = Constants.ANY_STRING.getValue();
        List<XmlJmsMessageDriverChannelAdapter> adapters = new ArrayList<>();
        try (Scanner scanner = new Scanner(xml)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.matches(anyString + XmlTag.JMS_MESSAGE_DRIVEN_CHANNEL_ADAPTER_OPEN.getValue() + anyString)) {
                    XmlJmsMessageDriverChannelAdapter adapter = getXmlJmsMessageDrivenChannelAdapter(line, scanner);
                    adapters.add(adapter);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return adapters;
    }

    public static List<XmlServiceActivator> readXmlServiceActivators(File xml) {
        List<XmlServiceActivator> activators = new ArrayList<>();
        try (Scanner scanner = new Scanner(xml)) {
        String anyString = Constants.ANY_STRING.getValue();
        boolean chained = false;
        boolean closeShort = true;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.matches(anyString + XmlTag.CHAIN_OPEN.getValue() + anyString)) chained = true;
                if (line.matches(anyString + XmlTag.SERVICE_ACTIVATOR_OPEN.getValue() + anyString)) {
                    XmlServiceActivator activator = getXmlServiceActivator(line, scanner, chained);
                    activators.add(activator);
                }
                if ((line.contains(XmlTag.CLOSE_SHORT.getValue()) && closeShort) || line.matches(anyString + XmlTag.CHAIN_CLOSE_LONG.getValue() + anyString))  chained = false;
                else closeShort = false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return activators;
    }

    private static XmlBean getXmlBean(String line, Scanner scanner) {
        XmlBean bean = new XmlBean();
        String tagIdValue = XmlTagValue.BEAN_ID.getValue();
        String tagClassNameValue = XmlTagValue.BEAN_CLASS.getValue();
        boolean closeShort = true;
        while (true) {
            if (line.contains(tagIdValue + "=")) {
                String id = getAllTagParamValues(tagIdValue, line).get(0);
                bean.setId(id);
            }
            if (line.contains(tagClassNameValue + "=")) {
                String className = getAllTagParamValues(tagClassNameValue, line).get(0);
                bean.setClassName(className);
            }
            if ((line.contains(XmlTag.CLOSE_SHORT.getValue())&& closeShort) || line.contains(XmlTag.BEAN_CLOSE_LONG.getValue()))
                break;
            else {
                line = scanner.nextLine();
                closeShort = false;
            }
        }
        return bean;
    }

    private static XmlProperty getXmlProperty(String line, Scanner scanner) {
        XmlProperty property = new XmlProperty();
        String tagNameValue = XmlTagValue.PROPERTY_NAME.getValue();
        String tagValueValue = XmlTagValue.PROPERTY_VALUE.getValue();
        String tagRefValue = XmlTagValue.PROPERTY_REF.getValue();
        while (true) {
            if (line.contains(tagNameValue + "=")) {
                String name = getAllTagParamValues(tagNameValue, line).get(0);
                property.setName(name);
            }
            if (line.contains(tagValueValue + "=")) {
                String value = getAllTagParamValues(tagValueValue, line).get(0);
                property.setValue(value);
            }
            if (line.contains(tagRefValue + "=")) {
                String ref = getAllTagParamValues(tagRefValue, line).get(0);
                property.setRef(ref);
            }
            if (line.contains(XmlTag.CLOSE_SHORT.getValue()) || line.contains(XmlTag.PROPERTY_CLOSE_LONG.getValue()))
                break;
            else line = scanner.nextLine();
        }
        return property;
    }

    private static XmlChain getXmlChain(String line, Scanner scanner) {
        XmlChain chain = new XmlChain();
        String anyString = Constants.ANY_STRING.getValue();
        String tagInputChannelValue = XmlTagValue.SPRING_INTEGRATION_INPUT_CHANNEL_ELEMENT_REF.getValue();
        String tagOutputChannelValue = XmlTagValue.SPRING_INTEGRATION_OUTPUT_CHANNEL_ELEMENT_REF.getValue();
        boolean closeShort = true;
        while (true) {
            if (line.matches(anyString + tagInputChannelValue + "=" + anyString)) {
                String inputChannel = getAllTagParamValues(tagInputChannelValue, line).get(0);
                chain.setInputChannel(inputChannel);
            }
            if (line.matches(anyString + tagOutputChannelValue + "=" + anyString)) {
                String outputChannel = getAllTagParamValues(tagOutputChannelValue, line).get(0);
                chain.setOutputChannel(outputChannel);
            }
            if ((line.contains(XmlTag.CLOSE_SHORT.getValue()) && closeShort) || line.matches(anyString + XmlTag.CHAIN_CLOSE_LONG.getValue() + anyString))
                break;
            else {
                line = scanner.nextLine();
                closeShort = false;
            }
        }
        return chain;
    }

    private static XmlJmsMessageDriverChannelAdapter getXmlJmsMessageDrivenChannelAdapter(String line, Scanner scanner) {
        XmlJmsMessageDriverChannelAdapter adapter = new XmlJmsMessageDriverChannelAdapter();
        String anyString = Constants.ANY_STRING.getValue();
        String tagChannelValue = XmlTagValue.SPRING_INTEGRATION_CHANNEL_ELEMENT_REF.getValue();
        String tagDestinationNameValue = XmlTagValue.SPRING_INTEGRATION_DESTINATION_NAME.getValue();
        while (true) {
            if (line.matches(anyString + tagChannelValue+ "=" + anyString)) {
                String channel = getAllTagParamValues(tagChannelValue, line).get(0);
                adapter.setChannel(channel);
            }
            if (line.matches(anyString + tagDestinationNameValue+ "=" + anyString)) {
                String destinationName = getAllTagParamValues(tagDestinationNameValue, line).get(0);
                adapter.setDestinationName(destinationName);
            }
            if (line.contains(XmlTag.CLOSE_SHORT.getValue()) || line.matches(anyString + XmlTag.JMS_MESSAGE_DRIVEN_CHANNEL_ADAPTER_CLOSE_LONG.getValue() + anyString))
                break;
            else line = scanner.nextLine();
        }
        return  adapter;
    }

    private static XmlServiceActivator getXmlServiceActivator(String line, Scanner scanner, boolean chained) {
        XmlServiceActivator activator = new XmlServiceActivator();
        activator.setChained(chained);
        String anyString = Constants.ANY_STRING.getValue();
        String tagRefValue = XmlTagValue.PROPERTY_REF.getValue();
        String tagClassValue = XmlTagValue.BEAN_CLASS.getValue();
        String tagInputChannelValue = XmlTagValue.SPRING_INTEGRATION_INPUT_CHANNEL_ELEMENT_REF.getValue();
        String tagOutputChannelValue = XmlTagValue.SPRING_INTEGRATION_OUTPUT_CHANNEL_ELEMENT_REF.getValue();
        while (true) {
            if (line.matches(anyString + tagRefValue+ "=" + anyString)) {
                String ref = getAllTagParamValues(tagRefValue, line).get(0);
                activator.setRef(ref);
            }
            if (line.matches(anyString + tagClassValue+ "=" + anyString)) {
                String className = getAllTagParamValues(tagClassValue, line).get(0);
                activator.setClassName(className);
            }
            if (line.matches(anyString + tagInputChannelValue+ "=" + anyString)) {
                String inputChannel = getAllTagParamValues(tagInputChannelValue, line).get(0);
                activator.setInputChannel(inputChannel);
            }
            if (line.matches(anyString + tagOutputChannelValue+ "=" + anyString)) {
                String outputChannel = getAllTagParamValues(tagOutputChannelValue, line).get(0);
                activator.setOutputChannel(outputChannel);
            }
            if (line.contains(XmlTag.CLOSE_SHORT.getValue()) || line.matches(anyString + XmlTag.SERVICE_ACTIVATOR_CLOSE_LONG.getValue() + anyString))
                break;
            else line = scanner.nextLine();
        }
        return activator;
    }

    public static String getFirstTagParamValue(String tagParamName, File xml) {
        String paramValue = "";
        try (Scanner scanner = new Scanner(xml)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(tagParamName)) {
                    paramValue = getAllTagParamValues(tagParamName + "=", line).get(0);
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  paramValue;
    }

    private static List<String> getAllTagParamValues(String tagParamName, File xml) {
        List<String>  paramValues = new ArrayList<>();
        try (Scanner scanner = new Scanner(xml)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(tagParamName)) {
                    paramValues.addAll(getAllTagParamValues(tagParamName + "=", line)) ;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return paramValues;
    }

    private static List<String> getAllTagParamValues(String tagParamName, String line) {
        List<String> paramValues = new ArrayList<>();
        String paramValue = "";
        List<String> split = Arrays.asList(line.split("\""));
        for (String entry : split) {
            if (entry.contains(tagParamName)) {
                try {
                    paramValue = split.get(split.indexOf(entry) + 1);
                    paramValues.add(paramValue);
                }
                catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
        return paramValues;
    }
}
