package ru.osipmd.rss;

import org.mockserver.integration.ClientAndServer;

import java.io.*;

import static java.util.stream.Collectors.joining;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;


public class FeedStubServer {

    private final int port;
    private final String baseUrl;

    private ClientAndServer mockServer;

    public FeedStubServer(int port) {
        this.port = port;
        this.baseUrl = "http://localhost:" + port;

    }

    public void start() {
        mockServer = startClientAndServer(port);
        addEndpoint("/atom", readFile("atom.xml"));
        addEndpoint("/rss2", readFile("rss2.0.xml"));

        addEndpoint("/atom_without_items", readFile("atom_without_items.xml"));
        addEndpoint("/rss_without_items", readFile("rss_without_items.xml"));

        addEndpoint("/atom_without_pub_date", readFile("atom_without_pub_date.xml"));
        addEndpoint("/rss_without_pub_date", readFile("rss_without_pub_date.xml"));
    }

    private void addEndpoint(String path, String response) {
        mockServer.when(
                request()
                        .withMethod("GET")
                        .withPath(path)
        )
                .respond(response()
                        .withStatusCode(200)
                        .withBody(response)
                );
    }

    public String getAtomUrl() {
        return baseUrl + "/atom";
    }

    public String getRssUrl() {
        return baseUrl + "/rss2";
    }

    public String getAtomWithoutItems() {
        return baseUrl + "/atom_without_items";
    }

    public String getAtomWithoutPubDate() {
        return baseUrl + "/atom_without_pub_date";
    }

    public String getRssWithoutPubDate() {
        return baseUrl + "/rss_without_pub_date";
    }

    public String getRssWithoutItems() {
        return baseUrl + "/rss_without_items";
    }

    public void stop() {
        mockServer.stop();
    }

    private String readFile(String name) {
        try (InputStream is = new FileInputStream(new File(getClass().getClassLoader().getResource(name).getFile()))) {
            BufferedReader input = new BufferedReader(new InputStreamReader(is));
            return input.lines().collect(joining());
        } catch (IOException ignored) {
        }
        return null;
    }
}
