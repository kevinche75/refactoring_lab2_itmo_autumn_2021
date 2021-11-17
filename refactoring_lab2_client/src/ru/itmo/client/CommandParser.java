package ru.itmo.client;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandParser {

    HashMap<String, ArrayList<Integer>> commands;

    public CommandParser(HashMap<String, ArrayList<Integer>> commands) {
        this.commands = commands;
    }

    private void checkLength(String[] commands, int min, int max) throws BadCommandException {
        if (commands.length - 1 < min || commands.length - 1 > max) {
            throw new BadCommandException("bad command");
        }
    }

    public String parseCommand(String line) throws BadCommandException {
        String[] parsedCommands = line.strip().split(" ");
        if (parsedCommands.length == 0) {
            throw new BadCommandException("bad command");
        }
        if (commands.containsKey(parsedCommands[0])) {
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
