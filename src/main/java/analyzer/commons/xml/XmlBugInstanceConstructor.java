package analyzer.commons.xml;

import analyzer.model.xml.generated.BugInstanceType;

/**
 * Created by NM.Rabotaev on 10.05.2017.
 */
public class XmlBugInstanceConstructor {

    public static BugInstanceType bugInstance(String detector, String type, String file, String message) {
        BugInstanceType bugInstance = new BugInstanceType();
        bugInstance.setDetector(detector);
        bugInstance.setType(type);
        bugInstance.setFile(file);
        bugInstance.setMessage(message);
        return bugInstance;
    }

}
