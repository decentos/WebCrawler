package com.monzo.service;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
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
    public void testCrawler() {
        Set<String> expectedResult = new HashSet<>(List.of("http://127.0.0.1:8081/", "http://127.0.0.1:8081/page2", "http://127.0.0.1:8081/page3", "http://127.0.0.1:8081/page4"));
        Set<String> actualResult = crawlerService.crawl("http://127.0.0.1:8081/");
        assertIterableEquals(expectedResult, actualResult);
    }
}