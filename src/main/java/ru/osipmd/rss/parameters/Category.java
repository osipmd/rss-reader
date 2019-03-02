package ru.osipmd.rss.parameters;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;

import java.util.List;

/**
 * It's class of Category parameter RSS channel
 * @author Mikhail Osipov
 */
public class Category extends RssParameter {

    private static final String NAME = "category";

    @Override
    String getInfoFromRssItem(SyndEntry item) {
        List<SyndCategory> categories = item.getCategories();
        StringBuilder categoriesString = new StringBuilder();
        for (SyndCategory category : categories) {
            categoriesString.append(category.getName());
        }
        return categoriesString.toString();
    }

    @Override
    String getParameterName() {
        return NAME;
    }
}
