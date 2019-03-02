package ru.osipmd.rss.parameters;

import com.rometools.rome.feed.synd.SyndEntry;

/**
 * It's the base class for RSS parameter types
 * @author Mikhail Osipov
 */
public abstract class RssParameter {

    /**
     * get parameter info from the item of RSS
     * @param item - one item of RSS
     * @return - parameterInfo
     */
    abstract String getInfoFromRssItem(SyndEntry item);

    /**
     * get parameter name
     * @return - parameter name
     */
    abstract String getParameterName();

    /**
     * get record parameterName: parameterInfo
     * @param item - one item of RSS
     * @return - record in the prescribed form
     */
    public String getRecord(SyndEntry item) {
        final String info = getInfoFromRssItem(item);
        if (info == null) {
            return "";
        }
        return getParameterName() + ":" + getInfoFromRssItem(item);
    }
}
