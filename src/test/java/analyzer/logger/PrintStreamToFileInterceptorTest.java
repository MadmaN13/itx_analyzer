package analyzer.logger;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by nikitarabotaev on 05.05.17.
 */
public class PrintStreamToFileInterceptorTest {

    @Test
    public void printLine() throws Exception {
        PrintStreamToFileInterceptor logger = new PrintStreamToFileInterceptor();
        logger.printLine("Hello! I am interceptor!");
    }

}