package ru.osipmd.rss;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import ru.osipmd.rss.parameters.RssParameter;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FeedHandler {

    public static SyndFeed getFeed(final String url) throws IOException, FeedException {
        final URL feedUrl = new URL(url);
        final SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(feedUrl));
    }

    public static String getNewItemsFromRss(final SyndFeed feed, final Long itemsAmount, final Set<RssParameter> parameters, final Date lastPollTime) {
        final List<SyndEntry> items = feed.getEntries().stream()
                .filter((i) -> lastPollTime.before(i.getPublishedDate()))
                .sorted(Comparator.comparing(SyndEntry::getPublishedDate).reversed())
                .limit(itemsAmount)
                .collect(Collectors.toList());
        final StringBuilder result = new StringBuilder();
        for (SyndEntry item : items) {
            result.append(getStringFromItem(item, parameters)).append("\n");
        }
        return result.toString();
    }

    private static String getStringFromItem(final SyndEntry item, final Set<RssParameter> parameters) {
        StringBuilder result = new StringBuilder();
        for (RssParameter parameter : parameters) {
            result.append(parameter.getRecord(item)).append("\n");
        }
        return result.toString();
    }
}
