package ru.osipmd.rss;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class RssHandlerTest {

    private static FeedStubServer stubServer;
    private RssHandler rssHandler;

    @BeforeClass
    public static void init() {
        stubServer = new FeedStubServer(8099);
        stubServer.start();
        new File(PropertiesHandler.getBaseRssConfigPath()).mkdir();
        new File(PropertiesHandler.getBaseStoragePath()).mkdir();
    }

    @AfterClass
    public static void stop() throws IOException {
        stubServer.stop();
        FileUtils.deleteDirectory(new File(PropertiesHandler.getBaseStoragePath()));
        FileUtils.deleteDirectory(new File(PropertiesHandler.getBaseRssConfigPath()));
    }

    @Before
    public void initHandler() {
        rssHandler = new RssHandler();
    }

    @Test
    public void when_add_new_rss() throws InterruptedException, IOException {
        String outputFileName = "test";
        List<String> parameters = new ArrayList<>();
        parameters.add("title");
        rssHandler.addRss(stubServer.getRssUrl(), outputFileName, 1L, 10L, parameters);
        Thread.currentThread().sleep(1500);
        rssHandler.turnOffRss(stubServer.getRssUrl());
        String content = String.join("\n", Files.readAllLines(Paths.get(PropertiesHandler.getBaseStoragePath() + File.separator + outputFileName + ".txt"), StandardCharsets.UTF_8));
        assertEquals("title:RSS Tutorial 0\n", content);
    }

    @Test
    public void check_what_task_is_scheduled() throws InterruptedException, IOException {
        String outputFileName = "test1";
        List<String> parameters = new ArrayList<>();
        parameters.add("title");
        rssHandler.addRss(stubServer.getRssUrl(), outputFileName, 1L, 10L, parameters);
        Thread.currentThread().sleep(4000);
        rssHandler.turnOffRss(stubServer.getRssUrl());
        String content = String.join("\n", Files.readAllLines(Paths.get(PropertiesHandler.getBaseStoragePath() + File.separator + outputFileName + ".txt"), StandardCharsets.UTF_8));
        assertEquals("title:RSS Tutorial 0\n" +
                "\n" +
                "title:RSS Tutorial 0\n" +
                "\n" +
                "title:RSS Tutorial 0\n", content);
    }

    @Test
    public void when_2_rss_write_to_one_file() throws InterruptedException, IOException {
        String outputFileName = "test2";
        List<String> parameters = new ArrayList<>();
        parameters.add("title");
        rssHandler.addRss(stubServer.getRssUrl(), outputFileName, 1L, 10L, parameters);
        rssHandler.addRss(stubServer.getAtomUrl(), outputFileName, 1L, 10L, parameters);
        Thread.currentThread().sleep(2000);
        rssHandler.turnOffRss(stubServer.getRssUrl());
        rssHandler.turnOffRss(stubServer.getAtomUrl());
        System.out.println();
        String expectation = "title:RSS Tutorial 0\ntitle:Atom-Powered Robots Run Amok 1\n";
        List<String> expectationList = Arrays.asList(expectation.split("\n"));
        expectationList.removeAll(Collections.singleton("\n"));
        List<String> result = Files.readAllLines(Paths.get(PropertiesHandler.getBaseStoragePath() + File.separator + outputFileName + ".txt"), StandardCharsets.UTF_8);
        result.removeAll(Collections.singleton("\n"));
        result.removeAll(Collections.singleton(""));
        assertTrue(CollectionUtils.isEqualCollection(expectationList, result));
    }
}