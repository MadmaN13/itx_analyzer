package analyzer.logger;

import org.junit.Test;

/**
 * Created by nikitarabotaev on 05.05.17.
 */
public class PrintStreamToFileInterceptorTest {

    @Test
    public void printLine() throws Exception {
        SysOutToFileInterceptor logger = new SysOutToFileInterceptor();
        logger.printLine("Hello! I am interceptor!");
    }

}