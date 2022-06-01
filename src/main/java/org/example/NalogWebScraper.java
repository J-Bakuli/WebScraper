package org.example;

import java.io.IOException;

import static org.example.FinalUrlFinder.findFinalUrl;
import static org.example.XmlProcessing.xmlProcessing;
import static org.example.ZipFileUtils.*;

public class NalogWebScraper {
    static final String url = "http://data.nalog.ru/opendata/7707329152-rafp/";
    static final String downloadPath = "D://rafp_data//rafp_data.zip"; //Change to your destination
    static final String extractPath = "D://rafp_data"; //Change to your destination
    static final String csvPath = "D://rafp_data/rafp_data.csv"; //Change to your destination

    public static void main(String[] args) throws IOException {
        downloadZip(findFinalUrl(url), downloadPath);
        extractAllFilesFromZip(downloadPath, extractPath);
        String lastVersionXmlPath = findExtractedFilePath(extractPath);
        xmlProcessing(lastVersionXmlPath, csvPath);
    }
}
