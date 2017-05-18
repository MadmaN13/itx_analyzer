package analyzer.detectors.spring.config;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by NM.Rabotaev on 12.05.2017.
 */
public class NonExistentChannelReferenceDetectorTest {
    @Test
    public void sawBug() throws Exception {
        NonExistentChannelReferenceDetector detector = new NonExistentChannelReferenceDetector(null);
        detector.sawBug();
    }

}