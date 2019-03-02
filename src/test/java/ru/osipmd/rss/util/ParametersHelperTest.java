package ru.osipmd.rss.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class ParametersHelperTest {

    @Test
    public void when_empty_params_list() {
        assertEquals(0, ParametersHelper.getParametersFromNames(Collections.emptyList()).size());
    }

    @Test
    public void when_wrong_param_name() {
        List<String> params = new ArrayList<>();
        params.add("titl");
        assertEquals(0, ParametersHelper.getParametersFromNames(params).size());
    }

    @Test
    public void when_one_title_param() {
        List<String> params = new ArrayList<>();
        params.add("title");
        assertEquals(1, ParametersHelper.getParametersFromNames(params).size());
    }

    @Test
    public void when_one_title_param_with_spaces() {
        List<String> params = new ArrayList<>();
        params.add("  title  ");
        assertEquals(1, ParametersHelper.getParametersFromNames(params).size());
    }

    @Test
    public void when_all_params() {
        List<String> params = new ArrayList<>();
        params.add("title");
        params.add("description");
        params.add("pubDate");
        params.add("guid");
        params.add("link");
        params.add("author");
        params.add("category");
        assertEquals(7, ParametersHelper.getParametersFromNames(params).size());
    }
}