package ru.itmo.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CommandParser {

    HashMap<String, ArrayList<Integer>> commands;

    public CommandParser(){
        commands = new HashMap<>();
        commands.put("add", new ArrayList<>(Arrays. asList(2,2)));
        commands.put("connect", new ArrayList<>(Arrays. asList(2,3)));
        commands.put("set", new ArrayList<>(Arrays. asList(2,2)));
        commands.put("print", new ArrayList<>(Arrays. asList(0,0)));
        commands.put("show", new ArrayList<>(Arrays. asList(0,1)));
        commands.put("help", new ArrayList<>(Arrays. asList(0,0)));
    }

    private void checkLength(String[] commands, int min, int max) throws BadCommandException {
        if (commands.length-1 < min || commands.length-1 > max){
            throw new BadCommandException("bad command");
        }
    }

    public String parseCommand(String line) throws BadCommandException {
        String[] parsedCommands = line.strip().split(" ");
        if (parsedCommands.length == 0){
            throw new BadCommandException("bad command");
        }
        if (commands.containsKey(parsedCommands[0])){
            checkLength(
                    parsedCommands,
                    commands.get(parsedCommands[0]).get(0),
                    commands.get(parsedCommands[0]).get(1)
            );
        } else {
            throw new BadCommandException("bad command");
        }
        return String.join("/", parsedCommands);
    }
}
