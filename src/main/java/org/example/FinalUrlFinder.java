package org.example;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

public class FinalUrlFinder {

    private static final Logger log = LoggerFactory.getLogger(FinalUrlFinder.class);

    static String findFinalUrl(String url) {
        String urlLastData = "";
        try {
            Document page = Jsoup.connect(url).get();
            Elements elementsContainer = page.getElementsByClass("border_table");
            Elements rows = elementsContainer.select("tr");

            for (int i = 1; i < rows.size(); i++) {
                Element row = rows.get(i);
                Elements cols = row.select("td");

                for (Element col : cols) {
                    if (col.text().equals("Гиперссылка (URL) на набор")) {
                        urlLastData = requireNonNull(col.nextElementSibling()).text();
                    }
                }
            }
            System.out.println("Last version of RAFP data " + urlLastData);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return urlLastData;
    }
}
