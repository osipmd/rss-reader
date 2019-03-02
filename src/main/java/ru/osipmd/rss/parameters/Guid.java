package ru.osipmd.rss.parameters;

import com.rometools.rome.feed.synd.SyndEntry;

/**
 * It's class of Guid parameter RSS channel
 * @author Mikhail Osipov
 */
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
