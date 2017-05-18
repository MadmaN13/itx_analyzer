package analyzer.commons.xml;

public interface SimpleMarshaller {
	
	/**
	 * Java object -> XML
	 * @param object
	 * @return
	 * @throws Exception 
	 */
	public String marshal(Object object) throws Exception;
	
	/**
	 * XML -> Java object
	 * @param text
	 * @return
	 * @throws Exception 
	 */
	public Object marshal(String text) throws Exception;

}
