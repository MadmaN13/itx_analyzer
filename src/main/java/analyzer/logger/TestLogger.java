package analyzer.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by nikitarabotaev on 05.05.17.
 */
public class TestLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestLogger.class);

    public static void logTestMessages() {
        LOGGER.trace("Trace");
        LOGGER.debug("Debug");
        LOGGER.info("Info");
        LOGGER.error("Error");
    }
}
