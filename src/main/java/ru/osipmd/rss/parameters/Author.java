package ru.osipmd.rss.parameters;

import com.rometools.rome.feed.synd.SyndEntry;

public class Author extends RssParameter {

    private static final String NAME = "author";

    @Override
    String getInfoFromRssItem(SyndEntry item) {
        return item.getAuthor();
    }

    @Override
    String getParameterName() {
        return NAME;
    }
}
