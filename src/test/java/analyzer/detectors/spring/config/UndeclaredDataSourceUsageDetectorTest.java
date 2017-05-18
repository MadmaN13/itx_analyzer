package analyzer.detectors.spring.config;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by NM.Rabotaev on 10.05.2017.
 */
public class UndeclaredDataSourceUsageDetectorTest {
    @Test
    public void sawBug() throws Exception {
        UndeclaredDataSourceUsageDetector detector = new UndeclaredDataSourceUsageDetector(null);
        detector.sawBug();
    }

}