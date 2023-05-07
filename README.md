# WebCrawler

## Monzo Task
We'd like you to write a simple web crawler in a programming language you're familiar with. Given a starting URL, the
crawler should visit each URL it finds on the same domain. It should print each URL visited, and a list of links found
on that page. The crawler should be limited to one subdomain - so when you start with *https://monzo.com/*, it would
crawl all pages on the monzo.com website, but not follow external links, for example to facebook.com or
community.monzo.com.

We would like to see your own implementation of a web crawler. Please do not use frameworks like scrapy or go-colly
which handle all the crawling behind the scenes or someone else's code. You are welcome to use libraries to handle
things like HTML parsing.

### Getting started
- Run `mvn compile` to obtain a runnable JAR
- Run `java -jar target/WebCrawler-1.0.0.jar`
- Open [Swagger page](http://localhost:8080/)
- Execute GET method [/api/v1/crawler/crawl](http://localhost:8080/swagger-ui/#/crawler-controller/crawlUsingGET) with parameters

### Parameters
- domain - Absolute URL: `https://monzo.com/`
- isRoot - Domain or subdomain: `true` (current domain like root URL) or `false` (need to find root)

### Algorith
- A queue is maintained for the crawlers to visit
- Convert the passed URL to a domain, if required
- Adding to the queue for parsing
- BFS approach - all URLs are added to the resulting set, and sub-URLs are added to the queue for parsing
- Processing is asynchronous in a separate thread
- If the queue is empty, we wait for one of the threads to execute until new values are added to the queue
- Crawling ends if the queue is empty and all threads have completed
- The results for each domain are cached
- Logs are written to a file
- In the response JSON with a list of URLs
