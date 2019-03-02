package ru.osipmd.rss.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class RssUrlHelperTest {

    @Test
    public void when_there_is_not_prohibt_symbols() {
        assertEquals("http:--static.feed.rbc.ru-rbc-logical-footer-news.rss", RssUrlHelper.getPathFromUrl("http:--static.feed.rbc.ru-rbc-logical-footer-news.rss"));
    }

    @Test
    public void when_there_is_prohibt_symbols() {
        assertEquals("http:--static.feed.rbc.ru-rbc-logical-footer-news.rss", RssUrlHelper.getPathFromUrl("http://static.feed.rbc.ru/rbc/logical/footer/news.rss"));
    }

}