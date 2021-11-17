package ru.itmo.client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RequestService {

    private String uri;
    private CommandParser commandParser;

    public RequestService(String uri) throws URISyntaxException, IOException, InterruptedException {
        this.uri = uri;
        commandParser = new CommandParser(getCommands());
    }

    private HashMap<String, ArrayList<Integer>> getCommands() throws URISyntaxException, IOException, InterruptedException {
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, ArrayList<Integer>>>(){}.getType();
        String commandsString = httpPostRequest("commands");
        return gson.fromJson(commandsString, type);
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
