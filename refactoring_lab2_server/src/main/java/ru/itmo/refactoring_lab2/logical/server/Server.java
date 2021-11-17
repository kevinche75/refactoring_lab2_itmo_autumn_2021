package ru.itmo.refactoring_lab2.logical.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.itmo.refactoring_lab2.logical.exceptions.UnknownCommand;
import ru.itmo.refactoring_lab2.logical.exceptions.ValueException;
import ru.itmo.refactoring_lab2.logical.ioValues.LogicInput;
import ru.itmo.refactoring_lab2.logical.ioValues.LogicOutput;
import ru.itmo.refactoring_lab2.logical.ioValues.interfaces.IOElement;
import ru.itmo.refactoring_lab2.logical.logicalFunctions.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Component
public class Server {

    @Autowired
    private HashMapLoaderSaver hashMapLoaderSaver;
    private HashMap<String, IOElement> elements;
    private LogicOutput output;

    @PostConstruct
    public void init() {
        try {
            elements = hashMapLoaderSaver.readHashMap();
        } catch (Exception e) {
            hashMapLoaderSaver.createNewFile();
            elements = new HashMap<>();
            System.out.println("Created new file");
        }
    }

    @PreDestroy
    private void saveHashMap(){
        try {
            hashMapLoaderSaver.writeHashMap(elements);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int parseInt(String strNumber) throws ValueException {
        int result;
        try {
            result = Integer.parseInt(strNumber);
        } catch (Exception e) {
            throw new ValueException("Can't parse int");
        }
        return result;
    }

    private boolean parseBoolean(String strBool) throws ValueException {
        boolean result;
        try {
            result = Boolean.parseBoolean(strBool);
        } catch (Exception e) {
            throw new ValueException("Can't parse boolean");
        }
        return result;
    }

    public String parseAdd(String element, String name) throws UnknownCommand, ValueException {
        if (element.isBlank() || name.isBlank()) {
            throw new UnknownCommand("Unknown format of add command");
        }
        if (elements.containsKey(name) || output != null && output.getName().equals(name)) {
            throw new ValueException(String.format("There is logical element with name: \"%s\"", name));
        }
        switch (element) {
            case (LogicInput.COMMAND_NAME) -> {
                LogicInput input = new LogicInput(name);
                elements.put(input.getName(), input);
            }
            case (LogicOutput.COMMAND_NAME) -> {
                if (output == null) {
                    output = new LogicOutput(name);
                } else {
                    throw new ValueException("Output has already been set");
                }
            }
            case (AND.COMMAND_NAME) -> {
                AND and = new AND(name);
                elements.put(and.getName(), and);
            }
            case (NOT.COMMAND_NAME) -> {
                NOT not = new NOT(name);
                elements.put(not.getName(), not);
            }
            case (OR.COMMAND_NAME) -> {
                OR or = new OR(name);
                elements.put(or.getName(), or);
            }
            case (XOR.COMMAND_NAME) -> {
                XOR xor = new XOR(name);
                elements.put(xor.getName(), xor);
            }
            default -> throw new UnknownCommand("Unknown format of add command");
        }
        return "Success";

    }

    public String parseConnect(String outputName, String inputName, String inputNumber) throws UnknownCommand, ValueException {
        if (outputName.isBlank() || inputName.isBlank() || (inputNumber != null && inputNumber.isEmpty())) {
            throw new UnknownCommand("Unknown format of add command");
        }
        if (output != null && output.getName().equals(outputName)) {
            throw new ValueException(String.format("Can't connect output element \"%s\", it's final element", outputName));
        }
        if (!elements.containsKey(outputName)) {
            throw new ValueException(String.format("No logical elements with such name: \"%s\"", outputName));
        }
        if (!elements.containsKey(inputName) && !(output != null && output.getName().equals(inputName))) {
            throw new ValueException(String.format("No logical elements with such name: \"%s\"", inputName));
        }
        IOElement firstElement = elements.get(outputName);
        IOElement secondElement;
        if (output != null && output.getName().equals((inputName))) {
            secondElement = output;
        } else {
            secondElement = elements.get(inputName);
        }
        if (secondElement instanceof LogicInput) {
            throw new ValueException(String.format("Can't set input logical element with name \"%s\" to logical element", secondElement.getName()));
        }
        if (inputNumber != null) {
            if (secondElement instanceof TwoParamsElement) {
                int port = parseInt(inputNumber);
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

    public String parseSet(String name, String value) throws UnknownCommand, ValueException {
        if (name.isBlank() || value.isBlank()) {
            throw new UnknownCommand("Unknown format of set command");
        }
        if (!elements.containsKey(name.strip())) {
            throw new ValueException(String.format("No logical elements with such name: \"%s\"", name));
        }
        IOElement inputElement = elements.get(name);
        if (!(inputElement instanceof LogicInput)) {
            throw new ValueException(String.format("Logical element with name: \"%s\" is not input", name));
        }
        boolean input = parseBoolean(value);
        inputElement.setInput(input);
        return "Success";
    }

    public String parsePrint() throws ValueException {
        if (output == null) {
            throw new ValueException("Output element is not set");
        }
        return output.getOutput().toString();
    }

    public String parseShow(String name) throws UnknownCommand, ValueException {
        if (name != null && name.isEmpty()) {
            throw new UnknownCommand("Unknown format of show command");
        }
        if (name == null) {
            StringJoiner result = new StringJoiner(" ");
            for (Map.Entry<String, IOElement> entry : elements.entrySet()) {
                String key = entry.getKey();
                IOElement value = entry.getValue();
                result.add(value.getCommandName());
            }
            if (output != null) {
                result.add(output.getCommandName());
            }
            return result.toString();
        } else {
            if (!elements.containsKey(name.strip()) && !(output != null && output.getName().equals(name))) {
                throw new ValueException(String.format("No logical elements with such name: \"%s\"", name));
            }
            if (output != null && output.getName().equals(name)) {
                return output.getFullName();
            } else {
                return elements.get(name).getFullName();
            }
        }
    }

    public String getHelp() {
        return """
                Commands:
                add {elemType} {name}: command to add element of {elemType} type. Possible {elemType} values: and, not, xor, or.
                add {in/out} {name}: command to add input or output for element, which added by user on previous step.
                connect {n} {m}: command to connect {n}’s output and {m}’s input.
                connect {n} {m} {0/1}: command to connect {n}’s output and {m}’s {0/1} input (only and, or, xor).
                print: command to display output value of the scheme.
                set {name} {false/true}: set input boolean value.
                show {name}: command to display information about the logical element: name of this element, names of blocks,
                show: command to display information about all logical elements,
                which connected with it or value on input(s). {name} is the number of logical element.
                help: print he
                exit: stop program;
                """;
    }
}
