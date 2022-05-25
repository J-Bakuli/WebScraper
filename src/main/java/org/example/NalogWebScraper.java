package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NalogWebScraper {
    public static void main(String[] args) {

        String url = "http://data.nalog.ru/opendata/7707329152-rafp/";
        //List<CompaniesExportData> companies = new LinkedList<>();
        String urlLastData = null;

        try {
            Document page = Jsoup.connect(url).get();
            Elements elementsContainer = page.getElementsByClass("border_table");
            Elements rows = elementsContainer.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                for (Element col : cols) {
                    if (col.text().equals("Гиперссылка (URL) на набор")) {
                        urlLastData = col.nextElementSibling().text();
                    }
                }
            }
            System.out.println(urlLastData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}