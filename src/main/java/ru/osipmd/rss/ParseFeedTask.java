package ru.osipmd.rss;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.apache.commons.lang3.StringUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class ParseFeedTask implements Runnable {

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

    private void writeFeedToFile(String items) throws IOException {
        synchronized (configuration.getOutputFileName()) {
            try (FileWriter wtiter = new FileWriter(configuration.getOutputFileName(), true)) {
                wtiter.write(items);
            }
        }
    }

    public RssConfiguration getConfiguration() {
        return configuration;
    }
}
