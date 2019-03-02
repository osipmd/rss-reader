package ru.osipmd.rss;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.osipmd.rss.parameters.Description;
import ru.osipmd.rss.parameters.RssParameter;
import ru.osipmd.rss.parameters.Title;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class FeedHandlerTest {

    private static FeedStubServer stubServer;

    @BeforeClass
    public static void init() {
        stubServer = new FeedStubServer(8099);
        stubServer.start();
    }

    @AfterClass
    public static void stop() {
        stubServer.stop();
    }

    @Test
    public void when_get_rss_feed_with_right_url() throws IOException, FeedException {
        SyndFeed feed = FeedHandler.getFeed(stubServer.getRssUrl());
        assertNotNull(feed);
        assertEquals(3, feed.getEntries().size());
    }


    @Test
    public void when_get_atom_feed_with_right_url() throws IOException, FeedException {
        SyndFeed feed = FeedHandler.getFeed(stubServer.getAtomUrl());
        assertNotNull(feed);
        assertEquals(2, feed.getEntries().size());
    }

    @Test
    public void when_get_rss_feed_without_items() throws IOException, FeedException {
        SyndFeed feed = FeedHandler.getFeed(stubServer.getRssWithoutItems());
        assertNotNull(feed);
        assertEquals(0, feed.getEntries().size());
    }

    @Test
    public void when_get_atom_feed_without_items() throws IOException, FeedException {
        SyndFeed feed = FeedHandler.getFeed(stubServer.getAtomWithoutItems());
        assertNotNull(feed);
        assertEquals(0, feed.getEntries().size());
    }

    @Test
    public void when_get_new_records_from_rss_feed() throws IOException, FeedException {
        SyndFeed feed = FeedHandler.getFeed(stubServer.getRssUrl());
        Set<RssParameter> parameters = new HashSet<>();
        parameters.add(new Title());
        parameters.add(new Description());
        String result = FeedHandler.getNewItemsFromRss(feed, 1L, parameters, DateUtils.addYears(new Date(), -10));
        assertNotNull(result);
        String expectation = "title:RSS Tutorial 0\n" +
                "description:New RSS tutorial on W3Schools 0\n\n";
        List<String> expectationList = Arrays.asList(expectation.split("\n"));
        expectationList.removeAll(Collections.singleton("\n"));
        List<String> resultList = Arrays.asList(result.split("\n"));
        resultList.removeAll(Collections.singleton("\n"));
        resultList.removeAll(Collections.singleton(""));
        assertTrue(CollectionUtils.isEqualCollection(expectationList, resultList));
    }

    @Test
    public void when_get_new_records_from_rss_feed_without_new_records() throws IOException, FeedException {
        SyndFeed feed = FeedHandler.getFeed(stubServer.getRssUrl());
        Set<RssParameter> parameters = new HashSet<>();
        parameters.add(new Title());
        parameters.add(new Description());
        String result = FeedHandler.getNewItemsFromRss(feed, 1L, parameters, DateUtils.addYears(new Date(), 100));
        assertTrue(result.isEmpty());
    }
}