package webcrawler;

import webcrawler.crawler.CrawlerController;

public class Application {
    
    public static void main(String[] args) {
        CrawlerController controller = new CrawlerController("http://www.sinergija.edu.ba", 1000);
        controller.start(new EmailCrawler(controller));
    }
    
}
