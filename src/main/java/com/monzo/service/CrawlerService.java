package com.monzo.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

@Service
public class CrawlerService {
    private static final Logger log = Logger.getLogger(CrawlerService.class.getName());

    public List<String> crawl(String domain) {
        Queue<String> heap = new ConcurrentLinkedQueue<>();
        Set<String> visited = ConcurrentHashMap.newKeySet();
        List<String> links = new ArrayList<>();

        heap.offer(domain);
        while (!heap.isEmpty()) {
            String link = heap.poll();
            links.add(link);
            parsePage(domain, link, visited, heap);
        }
        log.info("Successfully crawled %s and retrieved %d links.".formatted(domain, links.size()));
        return links;
    }

    private void parsePage(String domain, String link, Set<String> visited, Queue<String> heap) {
        if (visited.contains(link)) return;
        visited.add(link);

        try {
            Document doc = Jsoup.connect(link).get();
            Elements elements = doc.select("a[href^=" + domain + "]");
            elements.forEach(element -> heap.offer(element.attr("abs:href")));
        } catch (Exception e) {
            log.info(e.getMessage());
        }
    }
}
