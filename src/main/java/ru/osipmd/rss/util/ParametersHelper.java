package ru.osipmd.rss.util;

import ru.osipmd.rss.parameters.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * It's helper class for work with RSS parameters.
 * @author Mikhail Osipov
 */
public class ParametersHelper {

    private static final Map<String, Class> parametersMap = new ConcurrentHashMap<>();

    static {
        parametersMap.put("title", Title.class);
        parametersMap.put("description", Description.class);
        parametersMap.put("pubDate", PubDate.class);
        parametersMap.put("guid", Guid.class);
        parametersMap.put("link", Link.class);
        parametersMap.put("author", Author.class);
        parametersMap.put("category", Category.class);
    }

    /**
     * transform parameter's name list to parameter's object list
     * @param parameters - parameter's name list
     * @return - parameter's object list
     */
    public static Set<RssParameter> getParametersFromNames(final List<String> parameters) {
        final Set<RssParameter> rssParameters = new HashSet<>();
        for (String parameter : parameters) {
            Class parameterClass = parametersMap.get(parameter.trim());
            if (parameterClass == null) {
                continue;
            }
            try {
                rssParameters.add((RssParameter) parameterClass.getConstructor().newInstance());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ignored) {
            }
        }
        return rssParameters;
    }

}
