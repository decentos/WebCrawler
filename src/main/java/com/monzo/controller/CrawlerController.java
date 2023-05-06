package com.monzo.controller;

import com.monzo.service.CrawlerService;
import io.swagger.annotations.ApiParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/crawler")
public class CrawlerController {

    private final CrawlerService crawlerService;

    public CrawlerController(CrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping("/crawl")
    public ResponseEntity<List<String>> crawl(
            @ApiParam(name = "domain", value = "Domain URL", required = true) @RequestParam String domain
    ) {
        List<String> links = crawlerService.crawl(domain);
        return ResponseEntity.ok(links);
    }
}
