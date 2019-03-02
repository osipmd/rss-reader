package ru.osipmd.rss;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

/**
 * Runnable implementation for getting info from RSS in another Thread
 * @author Mikhail Osipov
 */
public class ParseFeedTask implements Runnable {

    /**
     * Configuration for Feed
     */
    private RssConfiguration configuration;

    ParseFeedTask(RssConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void run() {
        try {
            final SyndFeed feed = FeedHandler.getFeed(configuration.getUrl());
            String feedString = FeedHandler.getNewItemsFromRss(feed, configuration.getItemsAmount(), configuration.getParameters(), configuration.getLastPollTime());
            configuration.setLastPollTime(new Date());
            if (StringUtils.isNotBlank(feedString)) {
                writeFeedToFile(feedString);
            }
            PropertiesHandler.writeProperties(configuration);
        } catch (IOException | FeedException e) {
            System.out.println("Error during read feed: " + configuration.getUrl() + "\n" + e);
        }
    }

    /**
     * Write getting record to file
     * @param feedString - record
     * @throws IOException
     */
    private void writeFeedToFile(final String feedString) throws IOException {
        synchronized (configuration.getOutputFileName()) {
            try (FileWriter wtiter = new FileWriter(configuration.getOutputFileName(), true)) {
                wtiter.write(feedString);
            }
        }
    }

    /**
     * Get feed configuration
     * @return - feed configuration
     */
    public RssConfiguration getConfiguration() {
        return configuration;
    }
}
