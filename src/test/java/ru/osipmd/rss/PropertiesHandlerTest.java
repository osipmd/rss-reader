package ru.osipmd.rss;

import org.junit.Test;

import static org.junit.Assert.*;

public class PropertiesHandlerTest {

    @Test
    public void when_get_base_rss_config_path() {
        assertEquals("rss", PropertiesHandler.getBaseRssConfigPath());
    }

    @Test
    public void when_get_base_storage_path() {
        assertEquals("storage", PropertiesHandler.getBaseStoragePath());
    }
}