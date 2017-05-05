package analyzer.logger;

import java.io.*;

/**
 * Created by nikitarabotaev on 05.05.17.
 */
public class PrintStreamToFileInterceptor  {

    private final String fileName = "/Users/nikitarabotaev/Desktop/analyzerLogs/analyzerLog.log";
    private PrintStream stream;

    public PrintStreamToFileInterceptor() {
        super();
        stream = outputFile(fileName);
        if (stream != null) {
        System.setOut(stream);
        System.setErr(stream);
        }
    }

    public void printLine(String line) {
        System.out.println(line);
        stream.flush();
    }

    private PrintStream outputFile(String name) {
        try {
            return new PrintStream(new BufferedOutputStream(new FileOutputStream(name,true)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
