package ru.osipmd.rss.parameters;

import com.rometools.rome.feed.synd.SyndEntry;

public class Link extends  RssParameter {

    private static final String NAME = "link";

    @Override
    String getInfoFromRssItem(SyndEntry item) {
        return item.getLink();
    }

    @Override
    String getParameterName() {
        return NAME;
    }
}
