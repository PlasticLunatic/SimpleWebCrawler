package webcrawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import webcrawler.crawler.CrawlerController;
import webcrawler.crawler.Crawler;

import org.jsoup.nodes.Document;

/**
 *
 * @author Dusko
 */
public class EmailCrawler extends Crawler {
    
    private final Pattern emailPattern;
    private final Set<String> collectedEmails;

    public EmailCrawler(CrawlerController controller) {
        super(controller);
        
        this.emailPattern = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        this.collectedEmails = new HashSet<>();
    }

    @Override
    protected void handleVisit(Document doc) {
        String pageHtml = doc.html();
        Matcher m = emailPattern.matcher(pageHtml);
        while (m.find()) {
            String email = m.group();
            this.collectedEmails.add(email);
        }
    }

    @Override
    protected boolean shouldVisit(String url) {
        return true;
    }

    @Override
    protected void onFinish() {
        File file = new File(Paths.get("").toAbsolutePath().toString() + "\\crawler_data.txt");
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(EmailCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            
            for (String email : this.collectedEmails) {
                writer.write(email + "\n");
            }
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(EmailCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
