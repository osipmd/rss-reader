package ru.osipmd.rss;

import org.yaml.snakeyaml.Yaml;
import ru.osipmd.rss.util.ParametersHelper;
import ru.osipmd.rss.util.RssUrlHelper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropertiesHandler {

    private static final String BASE_RSS_CONFIG_PATH = getBaseRssConfigPath();
    private static final String BASE_STORAGE_PATH = getBaseStoragePath();
    private static final String YAML_EXT = ".yaml";
    private static final String TXT_EXT = ".txt";

    public static String getBaseRssConfigPath() {
        Properties prop = new Properties();
        try (InputStream inputStream = PropertiesHandler.class.getClassLoader().getResourceAsStream("application.properties")) {
            prop.load(inputStream);
        } catch (IOException e) {
            System.out.println("Error ");
        }
        return prop.getProperty("config.base.path");
    }

    public static String getBaseStoragePath() {
        Properties prop = new Properties();
        try (InputStream inputStream = PropertiesHandler.class.getClassLoader().getResourceAsStream("application.properties")) {
            prop.load(inputStream);
        } catch (IOException e) {
            System.out.println("Error during get properties");
        }
        return prop.getProperty("storage.base.path");
    }


    public static RssConfiguration readProperties(final String rssUrl) throws IOException {
        final Yaml yaml = new Yaml();
        final File configFile = new File(getPath(rssUrl));
        try (InputStream configFileStream = new FileInputStream(configFile)) {
            return yaml.load(configFileStream);
        }
    }

    public static List<RssConfiguration> readAllProperties() throws IOException {
        final List<RssConfiguration> configurations = new ArrayList<>();
        final Yaml yaml = new Yaml();
        try (Stream<Path> paths = Files.walk(Paths.get(BASE_RSS_CONFIG_PATH))) {
            List<File> configFiles = paths.filter(Files::isRegularFile).map(Path::toFile).collect(Collectors.toList());
            for (File configFile : configFiles) {
                try (InputStream configFileStream = new FileInputStream(configFile)) {
                    configurations.add(yaml.load(configFileStream));
                }
            }
            return configurations;
        }
    }

    public static void writeProperties(final RssConfiguration configuration) throws IOException {
        final Yaml yaml = new Yaml();
        final File configFile = new File(getPath(configuration.getUrl()));
        try (PrintWriter configWriter = new PrintWriter(new FileWriter(configFile))) {
            yaml.dump(configuration, configWriter);
        }
    }

    private static String getPath(final String url) {
        return BASE_RSS_CONFIG_PATH + File.separator + RssUrlHelper.getPathFromUrl(url) + YAML_EXT;
    }

    public static RssConfiguration createNewRssConfiguration(final String url, final String outputFileName, final Long pollPeriod,
                                                             final Long itemsAmount, final List<String> parameters) throws IOException {
        RssConfiguration configuration = RssConfiguration.builder()
                .url(url)
                .outputFileName(BASE_STORAGE_PATH + File.separator + outputFileName + TXT_EXT)
                .pollPeriod(pollPeriod)
                .itemsAmount(itemsAmount)
                .turnOn(true)
                .parameters(ParametersHelper.getParametersFromNames(parameters))
                .lastPollTime(new Date())
                .build();
        writeProperties(configuration);
        return configuration;
    }
}
