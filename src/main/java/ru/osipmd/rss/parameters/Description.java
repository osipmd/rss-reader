package ru.osipmd.rss.parameters;

import com.rometools.rome.feed.synd.SyndEntry;

public class Description extends RssParameter {

    private static final String NAME = "description";

    @Override
    String getInfoFromRssItem(SyndEntry item) {
        if (item.getDescription() == null) {
            return null;
        }
        return item.getDescription().getValue();
    }

    @Override
    String getParameterName() {
        return NAME;
    }
}
