package org.example;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Optional;

public class ZipFileUtils {

    private static final Logger log = LoggerFactory.getLogger(ZipFileUtils.class);

    @SuppressWarnings("SameParameterValue")
    static void downloadZip(String finalUrl, String downloadUrl) throws IOException {
        FileUtils.copyURLToFile(new URL(finalUrl), new File(downloadUrl));
        String zipFilePath = new File(downloadUrl).getAbsolutePath();
        System.out.println("Downloaded file path: " + zipFilePath);
    }

    @SuppressWarnings("SameParameterValue")
    static void extractAllFilesFromZip(String zipPath, String newPath) {
        try {
            ZipFile zipFile = new ZipFile(zipPath);
            zipFile.extractAll(newPath);
        } catch (ZipException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    @SuppressWarnings("SameParameterValue")
    static String findExtractedFilePath(String newPath) throws IOException {
        Path dir = Paths.get(newPath);
        String lastStringXmlFilePath = null;
        String lastXmlName;

        Optional<Path> lastXmlFilePath = Files.list(dir)
                .filter(f -> isXmlFile(String.valueOf(f)))
                .filter(f -> !Files.isDirectory(f))
                .max(Comparator.comparingLong(f -> f.toFile().lastModified()));

        if (lastXmlFilePath.isPresent()) {
            lastStringXmlFilePath = lastXmlFilePath.get().toString();
            lastXmlName = FilenameUtils.getName(String.valueOf(lastXmlFilePath)).replace("]", "");
            System.out.println(lastStringXmlFilePath);
            System.out.println("Last version xml filename: " + lastXmlName);
        }
        return lastStringXmlFilePath;
    }

    static boolean isXmlFile(String filename) {
        return filename.endsWith("XML");
    }
}
