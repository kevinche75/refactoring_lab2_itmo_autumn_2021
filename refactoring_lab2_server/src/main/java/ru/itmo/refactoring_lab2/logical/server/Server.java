package ru.itmo.refactoring_lab2.logical.server;

import ru.itmo.refactoring_lab2.logical.exceptions.UnknownCommand;
import ru.itmo.refactoring_lab2.logical.exceptions.ValueException;
import ru.itmo.refactoring_lab2.logical.ioValues.LogicInput;
import ru.itmo.refactoring_lab2.logical.ioValues.LogicOutput;
import ru.itmo.refactoring_lab2.logical.ioValues.interfaces.IOElement;
import ru.itmo.refactoring_lab2.logical.logicalFunctions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class Server {

    private HashMap<String, IOElement> elements;
    private LogicOutput output;
    private final String ADD_COMMAND = "add";
    private final String CONNECT_COMMAND = "connect";
    private final String SET_COMMAND = "set";
    private final String PRINT_COMMAND = "print";
    private final String SHOW_COMMAND = "show";
    private final String HELP_COMMAND = "help";

    public Server(){
        elements = new HashMap<>();
    }

    private int parseInt(String strNumber) throws ValueException {
        int result;
        try {
            result = Integer.parseInt(strNumber);
        } catch (Exception e){
            throw new ValueException("Can't parse int");
        }
        return result;
    }

    private boolean parseBoolean(String strBool) throws ValueException {
        boolean result;
        try {
            result = Boolean.parseBoolean(strBool);
        } catch (Exception e){
            throw new ValueException("Can't parse boolean");
        }
        return result;
    }

    private String parseAdd(String[] commands) throws UnknownCommand, ValueException {
        if(commands.length != 3){
            throw new UnknownCommand("Unknown format of add command");
        }
        if (elements.containsKey(commands[2]) || output != null && output.getName().equals(commands[2])){
            throw new ValueException(String.format("There is logical element with name: \"%s\"", commands[2]));
        }
        switch (commands[1]) {
            case (LogicInput.COMMAND_NAME) -> {
                LogicInput input = new LogicInput(commands[2]);
                elements.put(input.getName(), input);
            }
            case (LogicOutput.COMMAND_NAME) -> {
                if (output == null) {
                    output = new LogicOutput(commands[2]);
                } else {
                    throw new ValueException("Output has already been set");
                }
            }
            case (AND.COMMAND_NAME) -> {
                AND and = new AND(commands[2]);
                elements.put(and.getName(), and);
            }
            case (NOT.COMMAND_NAME) -> {
                NOT not = new NOT(commands[2]);
                elements.put(not.getName(), not);
            }
            case (OR.COMMAND_NAME) -> {
                OR or = new OR(commands[2]);
                elements.put(or.getName(), or);
            }
            case (XOR.COMMAND_NAME) -> {
                XOR xor = new XOR(commands[2]);
                elements.put(xor.getName(), xor);
            }
            default -> throw new UnknownCommand("Unknown format of add command");
        }
        return "Success";
    }

    private String parseConnect(String[] commands) throws UnknownCommand, ValueException {
        if(commands.length != 3 && commands.length != 4){
            throw new UnknownCommand("Unknown format of add command");
        }
        if (output != null && output.getName().equals(commands[1])){
            throw new ValueException(String.format("Can't connect output element \"%s\", it's final element", commands[1]));
        }
        if (!elements.containsKey(commands[1])){
            throw new ValueException(String.format("No logical elements with such name: \"%s\"", commands[1]));
        }
        if (!elements.containsKey(commands[2]) && !(output != null && output.getName().equals(commands[2]))){
            throw new ValueException(String.format("No logical elements with such name: \"%s\"", commands[2]));
        }
        IOElement firstElement = elements.get(commands[1]);
        IOElement secondElement;
        if (output != null && output.getName().equals((commands[2]))){
            secondElement = output;
        } else {
            secondElement = elements.get(commands[2]);
        }
        if (secondElement instanceof LogicInput){
            throw new ValueException(String.format("Can't set input logical element with name \"%s\" to logical element", secondElement.getName()));
        }
        if (commands.length == 4){
            if (secondElement instanceof TwoParamsElement) {
                int port = parseInt(commands[3]);
                if (port != 1 && port != 0) {
                    throw new UnknownCommand("Input port must be equal 0 or 1");
                }
                if (port == 0) {
                    ((TwoParamsElement) secondElement).setFirstInput(firstElement);
                } else {
                    ((TwoParamsElement) secondElement).setSecondInput(firstElement);
                }
            } else {
                throw new UnknownCommand(String.format("Element with name \"%s\"has no 2 input ports", secondElement.getName()));
            }
        } else {
            secondElement.setInput(firstElement);
        }
        return "Success";
    }

    private String parseSet(String[] commands) throws UnknownCommand, ValueException {
        if(commands.length != 3){
            throw new UnknownCommand("Unknown format of set command");
        }
        if (!elements.containsKey(commands[1].strip())){
            throw new ValueException(String.format("No logical elements with such name: \"%s\"", commands[1]));
        }
        IOElement inputElement = elements.get(commands[1]);
        if (!(inputElement instanceof LogicInput)){
            throw new ValueException(String.format("Logical element with name: \"%s\" is not input", commands[1]));
        }
        boolean input = parseBoolean(commands[2]);
        inputElement.setInput(input);
        return "Success";
    }

    private String parsePrint() throws ValueException {
        if (output == null){
            throw new ValueException("Output element is not set");
        }
        return output.getOutput().toString();
    }

    private String parseShow(String[] commands) throws UnknownCommand, ValueException {
        if (commands.length != 1 && commands.length != 2){
            throw new UnknownCommand("Unknown format of show command");
        }
        if (commands.length == 1){
            StringJoiner result = new StringJoiner(" ");
            for (Map.Entry<String, IOElement> entry : elements.entrySet()) {
                String key = entry.getKey();
                IOElement value = entry.getValue();
                result.add(value.getCommandName());
            }
            if (output != null){
                result.add(output.getCommandName());
            }
            return result.toString();
        } else {
            if (!elements.containsKey(commands[1].strip()) && !(output != null && output.getName().equals(commands[1]) )){
                throw new ValueException(String.format("No logical elements with such name: \"%s\"", commands[1]));
            }
            if (output != null && output.getName().equals(commands[1])){
                return output.getFullName();
            } else {
                return elements.get(commands[1]).getFullName();
            }
        }
    }

    public String parseCommand(String line){
        try {
            String[] commands = line.strip().split(" ");
            switch (commands[0].toLowerCase()) {
                case (ADD_COMMAND) -> {
                    return parseAdd(commands);
                }
                case (CONNECT_COMMAND) -> {
                    return parseConnect(commands);
                }
                case (SET_COMMAND) -> {
                    return parseSet(commands);
                }
                case (PRINT_COMMAND) -> {
                    return parsePrint();
                }
                case (SHOW_COMMAND) -> {
                    return parseShow(commands);
                }
                case (HELP_COMMAND) -> {
                    return getHelp();
                }
                default -> throw new UnknownCommand("Unknown command");
            }
        } catch (ValueException e){
            return e.getMessage();
        } catch (UnknownCommand e){
            return e.getMessage() + "\n" + getHelp();
        }
    }

    private String getHelp(){
        return """
                Commands:
                add {elemType} {name}: command to add element of {elemType} type. Possible {elemType} values: and, not, xor, or.
                add {in/out} {name}: command to add input or output for element, which added by user on previous step.
                connect {n} {m}: command to connect {n}’s output and {m}’s input.
                connect {n} {m} {0/1}: command to connect {n}’s output and {m}’s {0/1} input (only and, or, xor).
                print: command to display output value of the scheme.
                show {name}: command to display information about the logical element: name of this element, names of blocks,
                show: command to display information about all logical elements,
                which connected with it or value on input(s). {name} is the number of logical element.
                exit: stop program;
                """;
    }
}
