package webcrawler.crawler;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.UnsupportedMimeTypeException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author Dusko
 */
public abstract class Crawler {
    
    // We use fake USER_AGENT so the server will think that the robot is normal browser
    protected static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
    
    // HTTP ERROR code indicating that connection was unsuccessfull
    protected static final int HTTP_ERROR = 404;
    
    protected final CrawlerController controller;

    public Crawler(CrawlerController controller) {
        this.controller = controller;
    }
    
    /**
     * Tells crawler to visit the specified page
     * 
     * @param url URL of a page to be visited
     */
    public void visit(String url) {
        if (!shouldVisit(url)) {
            System.err.println("not visit");
            return;
        }
        
        Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
        if (connection.response().statusCode() == HTTP_ERROR) {
            System.err.println("not http ok");
            return;
        }
        
        try {
            Document doc = connection.get();
            handleVisit(doc);
            
            // Adds the url to the visited links
            this.controller.addToVisited(url);
            
            // Selects all links from the page
            Elements linksOnPage = doc.select("a[href]");
            
            // Collects all links crawler has not visited
            linksOnPage.forEach(link -> {
                String linkUrl = link.absUrl("href");
                if (!this.controller.isVisited(linkUrl)) {
                    this.controller.addToVisit(linkUrl);
                }
            });
        } catch (UnsupportedMimeTypeException ex) {
            // We ignore this exception, because it is only thrown if we try to crawl pdf/media files
        } catch (IOException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Defines what should the crawler do once it visits the page
     * 
     * @param doc HTML document of the page
     */
    protected abstract void handleVisit(Document doc);
    
    
    /**
     * Checks if we should visit the page
     * 
     * @param url URL of page
     * @return true or false, based on if we should visit the web page or not
     */
    protected abstract boolean shouldVisit(String url);
    
    /**
     * Defines what should the crawler do once the crawling has been finished,
     * stopped or interrupted
     */
    protected abstract void onFinish();
    
}
