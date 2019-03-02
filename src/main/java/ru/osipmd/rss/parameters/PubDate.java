package ru.osipmd.rss.parameters;

import com.rometools.rome.feed.synd.SyndEntry;

public class PubDate extends RssParameter {

    private static final String NAME = "pubDate";

    @Override
    String getInfoFromRssItem(SyndEntry item) {
        if (item.getPublishedDate() == null) {
            return null;
        }
        return item.getPublishedDate().toString();
    }

    @Override
    String getParameterName() {
        return NAME;
    }
}
