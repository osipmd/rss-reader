package ru.osipmd.rss.util;

import java.io.File;

/**
 * It's helper class for removing prohibited symbols from url if it's need to use url as path to file
 * @author Mikhail Osipov
 */
public class RssUrlHelper {

    /**
     * Replace prohibited symbol '/' in url to '-'
     * @param url - source url
     * @return - url with replaced symbols
     */
    public static String getPathFromUrl(final String url) {
        return url.replaceAll(File.separator, "-");
    }

}
