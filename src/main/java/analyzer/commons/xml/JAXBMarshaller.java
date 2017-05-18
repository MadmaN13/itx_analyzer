package analyzer.commons.xml;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class JAXBMarshaller implements SimpleMarshaller {
	
	private static final Logger LOGGER = LoggerFactory
			.getLogger(JAXBMarshaller.class);
	
	private JAXBContext context;
	private Unmarshaller unmarshaller;
	private Marshaller marshaller;
	
	public JAXBMarshaller(Class<? extends Object> clazz) throws JAXBException {
		setContext(JAXBContext.newInstance(clazz));
		setMarshaller(getContext().createMarshaller());
		setUnmarshaller(getContext().createUnmarshaller());
	}
	public JAXBMarshaller(String classes) throws JAXBException {
		if (!classes.isEmpty()) {
			setContext(JAXBContext.newInstance(classes));
			setMarshaller(getContext().createMarshaller());
			setUnmarshaller(getContext().createUnmarshaller());
		}
	}

	@Override
	public String marshal(Object object) throws Exception {
		String result = null;
		StringWriter writer = new StringWriter();
		getMarshaller().marshal(object, writer);
		result = writer.toString();
		writer.close();
		return result;
	}

	@Override
	public Object marshal(String text) throws Exception {
		Object result = null;
		result = getUnmarshaller().unmarshal(new StreamSource(new StringReader(text)));
		return result;
	}
	
	public Object marshal(Node node) throws JAXBException {
		Object result = null;
		result = getUnmarshaller().unmarshal(node);
		return result;
	}
	

	// ACCESSORS
	private JAXBContext getContext() {
		return context;
	}
	public void setContext(JAXBContext context) {
		this.context = context;
	}
	private Unmarshaller getUnmarshaller() {
		return unmarshaller;
	}
	public void setUnmarshaller(Unmarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}
	private Marshaller getMarshaller() {
		return marshaller;
	}
	public void setMarshaller(Marshaller marshaller) {
		this.marshaller = marshaller;
	}

}
