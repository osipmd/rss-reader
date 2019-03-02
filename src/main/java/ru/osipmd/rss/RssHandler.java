package ru.osipmd.rss;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Base class for working with RSS.
 * Create and start new RSS.
 * Change already created and running RSS.
 * Turn on, turn off RSS.
 *
 * @author Mikhail Osipov
 */
public class RssHandler {

    private final ConcurrentHashMap<String, ParseFeedTask> tasks = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, ScheduledFuture> futures = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

    /**
     * Create and start new RSS
     * @param url - rss url
     * @param outputFileName - output file name
     * @param pollPeriod - poll period
     * @param itemsAmount - item amount
     * @param parameters - parameters
     */
    public void addRss(final String url, final String outputFileName, final Long pollPeriod, final Long itemsAmount,
                       final List<String> parameters) {
        try {
            if (isNotValidRss(url)) {
                System.out.println("Rss is not valid.");
                return;
            }
            RssConfiguration configuration = PropertiesHandler.createNewRssConfiguration(url, outputFileName, pollPeriod, itemsAmount, parameters);
            ParseFeedTask task = new ParseFeedTask(configuration);
            ScheduledFuture future = scheduledExecutorService.scheduleWithFixedDelay(task, configuration.getPollPeriod(), configuration.getPollPeriod(), TimeUnit.SECONDS);
            futures.put(url, future);
            tasks.put(url, task);
        } catch (IOException e) {
            System.out.println(String.format("An error occurred during the adding feed %s", url));
        } catch (FeedException e) {
            System.out.println(String.format("Sorry, but feed %s is not valid.", url));
        }
    }

    /**
     * Restore all previously created RSS from configuration files
     */
    public void restoreAllRssFromConfig() {
        try {
            List<RssConfiguration> configurations = PropertiesHandler.readAllProperties();
            configurations.forEach(this::addRss);
        } catch (IOException e) {
            System.out.println("An error occurred during restoring all rss");
        }
    }

    /**
     * Create RSS from {@link ru.osipmd.rss.RssConfiguration}. Start RSS if turnOn is true
     * @param configuration {@link ru.osipmd.rss.RssConfiguration}
     */
    private void addRss(final RssConfiguration configuration) {
        final String url = configuration.getUrl();
        try {
            if (isNotValidRss(url)) {
                System.out.println("Rss is not valid.");
                return;
            }
            ParseFeedTask task = new ParseFeedTask(configuration);
            tasks.put(url, task);
            if (configuration.getTurnOn()) {
                ScheduledFuture future = scheduledExecutorService.scheduleWithFixedDelay(task, configuration.getPollPeriod(), configuration.getPollPeriod(), TimeUnit.SECONDS);
                futures.put(url, future);
            }
        } catch (IOException e) {
            System.out.println(String.format("An error occurred during the adding feed %s", url));
            System.out.println(e);
        } catch (FeedException e) {
            System.out.println(String.format("Sorry, but feed %s is not valid.", url));
        }
    }


    /**
     * Check rss
     * If rss hasn't items or pubDate tag - is not valid
     * @param url - rss url
     * @return - check result
     * @throws FeedException
     * @throws IOException
     */
    private boolean isNotValidRss(final String url) throws FeedException, IOException {
        final SyndFeed feed = FeedHandler.getFeed(url);
        final List<SyndEntry> items = feed.getEntries();
        if (items.isEmpty()) {
            return true;
        }
        for (SyndEntry item : items) {
            if (item.getPublishedDate() == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Change output file name for RSS
     * @param url - rss url
     * @param newOutputFileName - new file output name
     */
    public void changeOutputFileName(final String url, final String newOutputFileName) {
        ParseFeedTask task = tasks.get(url);
        if (task == null) {
            System.out.println(String.format("Rss with url %s is not found", url));
            return;
        }
        RssConfiguration configuration = task.getConfiguration();
        configuration.setOutputFileName(newOutputFileName);
        try {
            PropertiesHandler.writeProperties(configuration);
        } catch (IOException e) {
            System.out.println(String.format("An error occurred during the change output file name for feed %s", url));
        }
    }

    /**
     * Change items amount for RSS
     * @param url - rss url
     * @param newAmount - new fitems amount
     */
    public void changeAmountOfItems(final String url, final Long newAmount) {
        ParseFeedTask task = tasks.get(url);
        if (task == null) {
            System.out.println(String.format("Rss with url %s is not found", url));
            return;
        }
        RssConfiguration configuration = task.getConfiguration();
        configuration.setItemsAmount(newAmount);
        try {
            PropertiesHandler.writeProperties(configuration);
        } catch (IOException e) {
            System.out.println(String.format("An error occurred during the change items amount for feed %s", url));
        }
    }

    /**
     * change poll period for RSS
     * @param url - RSS url
     * @param newPollPeriod - new poll period
     */
    public void changePollPeriod(final String url, final Long newPollPeriod) {
        ParseFeedTask task = tasks.get(url);
        if (task == null) {
            System.out.println(String.format("Rss with url %s is not found", url));
            return;
        }
        RssConfiguration configuration = task.getConfiguration();
        configuration.setPollPeriod(newPollPeriod);
        try {
            PropertiesHandler.writeProperties(configuration);
            if (configuration.getTurnOn()) {
                ScheduledFuture future = futures.get(url);
                if (future == null) {
                    return;
                }
                future.cancel(false);
                future = scheduledExecutorService.scheduleAtFixedRate(task, configuration.getPollPeriod(), configuration.getPollPeriod(), TimeUnit.SECONDS);
                futures.put(url, future);
            }
        } catch (IOException e) {
            System.out.println(String.format("An error occurred during the change poll period for feed %s", url));
        }
    }

    /**
     * Turn on RSS feed
     * @param url - RSS url
     */
    public void turnOnRss(final String url) {
        ParseFeedTask task = tasks.get(url);
        if (task == null) {
            System.out.println(String.format("Rss with url %s is not found", url));
            return;
        }
        RssConfiguration configuration = task.getConfiguration();
        if (configuration.getTurnOn()) {
            System.out.println("Rss is already running");
            return;
        }
        configuration.setTurnOn(true);
        try {
            PropertiesHandler.writeProperties(configuration);
            ScheduledFuture future = scheduledExecutorService.scheduleWithFixedDelay(task, configuration.getPollPeriod(), configuration.getPollPeriod(), TimeUnit.SECONDS);
            futures.put(url, future);
            System.out.println(String.format("Rss %s is running", url));
        } catch (IOException e) {
            System.out.println(String.format("An error occurred during the change poll period for feed %s", url));
        }
    }

    /**
     * Turn off RSS feed
     * @param url - RSS url
     */
    public void turnOffRss(final String url) {
        ParseFeedTask task = tasks.get(url);
        if (task == null) {
            System.out.println(String.format("Rss with url %s is not found", url));
            return;
        }
        RssConfiguration configuration = task.getConfiguration();
        if (!configuration.getTurnOn()) {
            System.out.println("Rss is already stopped");
            return;
        }
        configuration.setTurnOn(false);
        try {
            PropertiesHandler.writeProperties(configuration);
            ScheduledFuture future = futures.get(url);
            if (future == null) {
                return;
            }
            future.cancel(false);
            System.out.println(String.format("Rss %s is stopped", url));
        } catch (IOException e) {
            System.out.println(String.format("An error occurred during the change poll period for feed %s", url));
        }
    }

    /**
     * Print list of running rss
     */
    public void printRunningRss() {
        for (Map.Entry<String, ParseFeedTask> task : tasks.entrySet()) {
            if (task.getValue().getConfiguration().getTurnOn()) {
                System.out.println(String.format("on RSS : %s", task.getKey()));
            }
        }
    }

    /**
     * Print list of disabled rss
     */
    public void printDisabledRss() {
        for (Map.Entry<String, ParseFeedTask> task : tasks.entrySet()) {
            if (!task.getValue().getConfiguration().getTurnOn()) {
                System.out.println(String.format("off RSS : %s", task.getKey()));
            }
        }
    }

}
