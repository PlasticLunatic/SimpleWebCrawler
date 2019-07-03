package webcrawler.crawler;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Dusko
 */
public class CrawlerController {
    
    private final Set<String> visitedLinks;
    private final List<String> linksToVisit;
    
    private final int maxLinksToBeVisited;
    
    private boolean searching;

    public CrawlerController(String startingUrl, int maxLinksToBeVisited) {
        this.visitedLinks = new HashSet<>();
        this.linksToVisit = new LinkedList<>();
        
        this.maxLinksToBeVisited = maxLinksToBeVisited;
        
        this.searching = false;
        
        this.linksToVisit.add(startingUrl);
    }
    
    /**
     * Starts crawling with the specified crawler implementation
     * 
     * @param crawler Crawler implementation which we will use for crawling
     */
    public void start(Crawler crawler) {
        if (this.searching) {
            return;
        }
        this.searching = true;
        
        Thread thread = new Thread(() -> handleCrawling(crawler));
        thread.start();
    }
    
    /**
     * Tells the crawler which links it should visit
     * 
     * @param crawlerr Crawler implementation which we will use for crawling
     */
    private void handleCrawling(Crawler crawler) {
        while (this.searching) {
            if (this.visitedLinks.size() >= this.maxLinksToBeVisited) {
                break;
            }
            
            // We snapshot a list of links to be visited
            List<String> linksToVisitCopy = new LinkedList<>(this.linksToVisit);
            linksToVisitCopy.forEach(crawler::visit);
        }
        crawler.onFinish();
    }

    /**
     * Stops the crawling operation
     */
    public void stop() {
        this.searching = false;
        this.linksToVisit.clear();
    }
    
    /**
     * Checks if the crawler already visited the specified URL
     * 
     * @param url URL to be checked
     * @return true or false, based on if the URL is visited or not
     */
    public boolean isVisited(String url) {
        return visitedLinks.contains(url);
    }
    
    /**
     * Adds the specified URL to the list of URLs which will be visited
     * 
     * @param url URL to add to URLs we should visit
     */
    public void addToVisit(String url) {
        linksToVisit.add(url);
    }

    /**
     * Adds the specified URL to the list of visited URLs
     * 
     * @param url URL to add to visited URLs
     */
    public void addToVisited(String url) {
        visitedLinks.add(url);
        linksToVisit.remove(url);
    }
    
}
