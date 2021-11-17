package ru.itmo.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestService {

    private String uri;
    private CommandParser commandParser;

    public RequestService(String uri) {
        this.uri = uri;
        commandParser = new CommandParser();
    }

    public String execute(String line) throws URISyntaxException, IOException, InterruptedException {
        try {
            return httpPostRequest(
                    commandParser.parseCommand(line)
            );
        } catch (BadCommandException e) {
            return httpPostRequest("help");
        }
    }

    private String httpPostRequest(String command) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(uri + "/" + command))
                .version(HttpClient.Version.HTTP_2)
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response.body();
    }
}
