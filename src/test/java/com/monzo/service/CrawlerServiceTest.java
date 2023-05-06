package com.monzo.service;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static util.TestUtil.stubURIWithFilename;

public class CrawlerServiceTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(WireMockConfiguration.wireMockConfig().port(8081));
    private final CrawlerService crawlerService = new CrawlerService();

    @Before
    public void setupSite() {
        stubURIWithFilename("/", "index.html");
        stubURIWithFilename("/page2", "page2.html");
        stubURIWithFilename("/page3", "page3.html");
        stubURIWithFilename("/page4", "page4.html");
    }

    @Test
    public void testDomainCrawler() {
        Set<String> expectedResult = new HashSet<>(List.of("http://127.0.0.1:8081/", "http://127.0.0.1:8081/page2", "http://127.0.0.1:8081/page3", "http://127.0.0.1:8081/page4"));
        Set<String> actualResult = crawlerService.crawl("http://127.0.0.1:8081/", true);
        assertIterableEquals(expectedResult, actualResult);
    }

    @Test
    public void testSubDomainCrawler() {
        Set<String> expectedResult = new HashSet<>(List.of("http://127.0.0.1:8081/", "http://127.0.0.1:8081/page2", "http://127.0.0.1:8081/page3", "http://127.0.0.1:8081/page4"));
        Set<String> actualResult = crawlerService.crawl("http://127.0.0.1:8081/page2", false);
        assertIterableEquals(expectedResult, actualResult);
    }

    @Test
    public void testSubDomainLikeRootCrawler() {
        Set<String> expectedResult = Set.of("http://127.0.0.1:8081/page2");
        Set<String> actualResult = crawlerService.crawl("http://127.0.0.1:8081/page2", true);
        assertIterableEquals(expectedResult, actualResult);
    }

    @Test
    public void testNotAbsolutUrl() {
        Throwable exception = assertThrows(CompletionException.class, () -> crawlerService.crawl("127.0.0.1:8081", true));
        assertEquals("The supplied URL, '127.0.0.1:8081', is malformed. Make sure it is an absolute URL, and starts with 'http://' or 'https://'. See https://jsoup.org/cookbook/extracting-data/working-with-urls",
                exception.getCause().getMessage());
    }
}