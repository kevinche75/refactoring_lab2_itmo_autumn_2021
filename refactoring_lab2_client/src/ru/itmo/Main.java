package ru.itmo;

import ru.itmo.client.Client;

import java.io.IOException;
import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException, InterruptedException {
        if (args.length == 0 || args[0].isBlank()){
            System.out.println("Specify host address with args");
        } else {
            Client client = new Client(args[0]);
            client.launchClient();
        }
    }
}
