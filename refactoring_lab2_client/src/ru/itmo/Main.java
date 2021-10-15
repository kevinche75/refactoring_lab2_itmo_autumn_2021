package ru.itmo;

import ru.itmo.client.Client;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        Client client = new Client("http://localhost:8080/logical/command");
        client.launchClient();
    }
}
