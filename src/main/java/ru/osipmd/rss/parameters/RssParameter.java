package ru.osipmd.rss.parameters;

import com.rometools.rome.feed.synd.SyndEntry;

public abstract class RssParameter {

    abstract String getInfoFromRssItem(SyndEntry item);

    abstract String getParameterName();

    public String getRecord(SyndEntry item) {
        final String info = getInfoFromRssItem(item);
        if (info == null) {
            return "";
        }
        return getParameterName() + ":" + getInfoFromRssItem(item);
    }
}
