package analyzer.detectors.spring.config;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by NM.Rabotaev on 12.05.2017.
 */
public class NonExistentChannelDeclarationDetectorTest {

    @Test
    public void sawBug() throws Exception {
        NonExistentChannelDeclarationDetector detector = new NonExistentChannelDeclarationDetector(null);
        detector.sawBug();
    }

}