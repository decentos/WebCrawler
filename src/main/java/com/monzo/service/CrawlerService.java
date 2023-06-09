package com.monzo.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

@Service
public class CrawlerService {
    private static final Logger log = Logger.getLogger(CrawlerService.class.getName());

    @Cacheable("pages")
    public Set<String> crawl(String domain, boolean isRoot) {
        Queue<String> heap = new ConcurrentLinkedQueue<>();
        Queue<CompletableFuture<Void>> threads = new ConcurrentLinkedQueue<>();
        Set<String> links = ConcurrentHashMap.newKeySet();

        String root = isRoot ? domain : removePath(domain);
        heap.offer(root);

        while (!heap.isEmpty()) {
            String link = heap.poll();
            threads.offer(CompletableFuture.runAsync(() -> parsePage(links, root, link, heap)));
            while (heap.isEmpty() && !threads.isEmpty()) threads.poll().join();
        }
        log.info("Successfully crawled %s and retrieved %d links.".formatted(domain, links.size()));
        return links;
    }

    private void parsePage(Set<String> links, String domain, String link, Queue<String> heap) {
        if (links.contains(link)) return;
        links.add(link);

        try {
            Document doc = Jsoup.connect(link).get();
            Elements elements = doc.select("a[href^=" + domain + "]");
            elements.forEach(element -> heap.offer(element.attr("abs:href")));
        } catch (IOException e) {
            log.info(e.getMessage());
        }
    }

    private String removePath(String domain) {
        try {
            URI uri = new URI(domain);
            return domain.replace(uri.getPath(), "/");
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException();
        }
    }
}
