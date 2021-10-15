package ru.itmo.refactoring_lab2.logical.logicalFunctions;


import ru.itmo.refactoring_lab2.logical.exceptions.ValueException;
import ru.itmo.refactoring_lab2.logical.ioValues.interfaces.IOElement;

public class NOT implements IOElement {

    public final static String COMMAND_NAME = "not";
    private final String name;
    IOElement input;

    public NOT(String name){
        this.name = name;
    }

    @Override
    public void setInput(Object value) {
        if (value instanceof IOElement) {
            input = (IOElement) value;
        }
    }

    @Override
    public String getCommandName() {
        return String.format("%s:%s", name, COMMAND_NAME);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean getOutput() throws ValueException {
        if (input == null){
            throw new ValueException("");
        }
        return !input.getOutput();
    }

    @Override
    public String getFullName() {
        String inputString = " ";
        if (input != null){
            inputString = input.getFullName();
        }
        return String.format("%s(%s)", getCommandName(), input);
    }
}
