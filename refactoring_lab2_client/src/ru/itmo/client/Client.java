package ru.itmo.client;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Client {

    private String uri;
    private boolean launch = true;

    public Client(String URI) throws IOException {
        this.uri = URI;
    }

    public void httpPostRequest(String command) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                .version(HttpClient.Version.HTTP_2)
                .POST(HttpRequest.BodyPublishers.ofString(command))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String responseBody = response.body();
        System.out.println(responseBody);
    }

    public void launchClient() throws URISyntaxException, IOException, InterruptedException {

            Scanner scanner = new Scanner(System.in);
            String line;
            httpPostRequest("help");
            while (launch){
                line = scanner.nextLine();
                if (line.toLowerCase().trim().equals("exit")){
                    launch = false;
                    break;
                }
                httpPostRequest(line);
            }
    }
}
