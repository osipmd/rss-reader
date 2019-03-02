package ru.osipmd.rss.parameters;

import com.rometools.rome.feed.synd.SyndEntry;

public class Title extends RssParameter {

    private static final String NAME = "title";

    @Override
    String getInfoFromRssItem(SyndEntry item) {
        return item.getTitle();
    }

    @Override
    String getParameterName() {
        return NAME;
    }
}
