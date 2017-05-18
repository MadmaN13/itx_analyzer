package analyzer.detectors.spring.config;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by NM.Rabotaev on 11.05.2017.
 */
public class DifferentSpringConfigFileNameDetectorTest {
    @Test
    public void sawBug() throws Exception {
        DifferentSpringConfigFileNameDetector detector = new DifferentSpringConfigFileNameDetector(null);
        detector.sawBug();
    }

}