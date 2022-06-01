package org.example;

import com.google.common.collect.Streams;
import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.apache.commons.text.WordUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class XmlProcessing {

    private static final Logger log = LoggerFactory.getLogger(XmlProcessing.class);

    public static void xmlProcessing(String xmlPath, String csvPath) {
        extractingExportDataFromXml(xmlPath);
        findNumberOfCompanies();
        savingToCsv(countryStatusNumber, csvPath);
    }

    private static final List<String> countries = new ArrayList<>();
    private static final List<String> statuses = new ArrayList<>();

    static void extractingExportDataFromXml(String xmlPath) {
        try {
            File xmlSource = new File(xmlPath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(xmlSource);

            XPathExpression countryStatusExpr = XPathFactory.newInstance().newXPath().compile("(//*[@НаимСтр])");
            Object countryStatusResult = countryStatusExpr.evaluate(document, XPathConstants.NODESET);
            NodeList countryStatusNodes = (NodeList) countryStatusResult;
            for (int i = 0; i < countryStatusNodes.getLength(); i++) {
                String country = countryStatusNodes.item(i).getAttributes().getNamedItem("НаимСтр").getNodeValue().trim();
                countries.add(WordUtils.capitalizeFully(country));
            }

            XPathExpression branchOfficeStatusesExpr = XPathFactory.newInstance().newXPath().compile("//*[@СостАк]");
            Object branchOfficeStatusesResult = branchOfficeStatusesExpr.evaluate(document, XPathConstants.NODESET);
            NodeList branchOfficeStatusesNodes = (NodeList) branchOfficeStatusesResult;
            for (int i = 0; i < branchOfficeStatusesNodes.getLength(); i++) {
                String status = branchOfficeStatusesNodes.item(i).getAttributes().getNamedItem("СостАк").getNodeValue().trim();
                statuses.add(status);
            }

        } catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    static List<String> countryStatusAsExtracted = new ArrayList<>();
    static List<String> countryStatusUnique = new ArrayList<>();
    static Map<String, Integer> countryStatusNumber = new HashMap<>();

    static void findNumberOfCompanies() {
        if (countries.size() == statuses.size()) {
            countryStatusAsExtracted = Streams.zip(countries.stream(), statuses.stream(), (a, b) -> (a + "," + b))
                    .collect(Collectors.toList());
        } else {
            log.error("countries.size() не равен statuses.size() - проверь данные");
            throw new RuntimeException("Wrong document data");
        }

        List<String> countriesDistinct = countries.stream().distinct().collect(Collectors.toList());
        List<String> statusesDistinct = statuses.stream().distinct().collect(Collectors.toList());

        for (String country : countriesDistinct) {
            for (String status : statusesDistinct) {
                countryStatusUnique.add(country + "," + status);
            }
        }

        for (String pair1 : countryStatusAsExtracted) {
            for (String pair2 : countryStatusUnique) {
                if (pair1.equals(pair2)) {
                    countryStatusNumber.put(pair2, countryStatusNumber.getOrDefault(pair2, 0) + 1);
                }
            }
        }
    }

    static void savingToCsv(Map<String, Integer> countryStatusNumber, String csvPath) {
        try (PrintWriter writer = new PrintWriter(csvPath)) {
            StringBuilder sb = new StringBuilder();
            Set<String> countryStatuses = countryStatusNumber.keySet();
            writer.write("Страна регистрации головной компании,Статус филиала/представительства,Количество компаний");
            for (String pair : countryStatuses) {
                sb.append('\n');
                sb.append(pair);
                sb.append(',');
                sb.append(countryStatusNumber.get(pair));
                sb.append('\n');
                writer.write(sb.toString());
                sb.setLength(0);
            }
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
            e.getStackTrace();
        }
    }
}