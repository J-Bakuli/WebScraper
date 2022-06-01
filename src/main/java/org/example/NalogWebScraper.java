package org.example;

import java.io.IOException;

import static org.example.FinalUrlFinder.findFinalUrl;
import static org.example.XmlProcessing.xmlProcessing;
import static org.example.ZipFileUtils.*;

public class NalogWebScraper {
    /*url - url для парсинга;
    extractPath - основной путь, куда будет сохранен архив данных после парсинга;
    downloadPath - путь с названием скачанного архива;
    csvPath - путь с названием обработанных данных в виде csv файла*/
    static final String url = "http://data.nalog.ru/opendata/7707329152-rafp/";
    static final String extractPath = "D://rafp_data"; //Поменять на нужный путь к папке
    static final String downloadPath = extractPath + "//rafp_data.zip";
    static final String csvPath = extractPath + "/rafp_data.csv";

    public static void main(String[] args) throws IOException {
        downloadZip(findFinalUrl(url), downloadPath);
        extractAllFilesFromZip(downloadPath, extractPath);
        String lastVersionXmlPath = findExtractedFilePath(extractPath);
        xmlProcessing(lastVersionXmlPath, csvPath);
    }
}
