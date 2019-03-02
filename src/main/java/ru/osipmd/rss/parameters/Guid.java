package ru.osipmd.rss.parameters;

import com.rometools.rome.feed.synd.SyndEntry;

public class Guid extends RssParameter {

    private static final String NAME = "guid";

    @Override
    String getInfoFromRssItem(SyndEntry item) {
        return item.getUri();
    }

    @Override
    String getParameterName() {
        return NAME;
    }
}
