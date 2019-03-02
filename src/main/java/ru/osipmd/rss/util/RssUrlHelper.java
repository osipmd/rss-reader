package ru.osipmd.rss.util;

import java.io.File;

public class RssUrlHelper {

    public static String getPathFromUrl(final String url) {
        return url.replaceAll(File.separator, "-");
    }

}
