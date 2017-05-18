package analyzer.commons;

import analyzer.detectors.spring.config.commons.Constants;
import analyzer.model.xml.XmlConfigFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Created by NM.Rabotaev on 08.05.2017.
 */
public class FileUtils {

    public static List<String> findJavaFiles() {
        File root = new File(Constants.ROOT.getValue());
        List<File> allFiles = listAllFiles(root);
        List<String> javaFiles = new ArrayList<>();
        for (File file:allFiles) {
            String path = file.getAbsolutePath();
            if (path.endsWith(Constants.JAVA_FILE_FLAG.getValue())) javaFiles.add(path);
        }
        return javaFiles;
    }

    public static List<XmlConfigFile> findXmlConfigFiles() {
        File root = new File(Constants.ROOT.getValue());
        List<File> allFiles = listAllFiles(root);
        List<XmlConfigFile> xmlConfigFiles = new ArrayList<>();
        for (File file:allFiles) {
            if (file.getAbsolutePath().endsWith(Constants.XML_FILE_FLAG.getValue())) {
                XmlConfigFile xmlFile = new XmlConfigFile(file);
                if (FileUtils.fileContains(file, Constants.FILE_IS_SPRING_INTEGRATION_CONFIG_FLAG.getValue())) {
                    xmlFile.setSpringConfig(true);
                    xmlFile.setSpringIntegrationService(true);
                }
                if (FileUtils.fileContains(file, Constants.FILE_IS_SPRING_BATCH_CONFIG_FLAG.getValue())) {
                    xmlFile.setSpringConfig(true);
                    xmlFile.setSpringBatchJob(true);
                }
                xmlConfigFiles.add(xmlFile);
            }
        }
        return xmlConfigFiles;
    }

    private static List<File> listAllFiles(File root) {
        List<File> allFiles = new ArrayList<>();
        if (root.exists()) {
            File[] files = root.listFiles();
            for (File file:files) {
                if (file.isFile()) allFiles.add(file);
                if (file.isDirectory()) allFiles.addAll(listAllFiles(file));
            }
        }
        return  allFiles;
    }

    public static boolean fileContains(File file, String toFind) {
        boolean result = false;
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.contains(toFind)) {
                    result = true;
                    break;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void writeBug(File dest, String bugXml) {
        try (FileWriter writer = new FileWriter(dest,true)) {
            writer.write(bugXml + "\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
