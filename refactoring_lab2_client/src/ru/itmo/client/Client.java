package ru.itmo.client;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Client {

    private RequestService requestService;
    private boolean launch = true;

    public Client(String URI) throws IOException, URISyntaxException, InterruptedException {
        requestService = new RequestService(URI);
    }

    public void launchClient() throws URISyntaxException, IOException, InterruptedException {

        Scanner scanner = new Scanner(System.in);
        String line;
        System.out.println(requestService.execute("help"));
        while (launch){
            line = scanner.nextLine();
            if (line.toLowerCase().trim().equals("exit")){
                launch = false;
                break;
            }
            System.out.println(requestService.execute(line));
        }
    }
}
