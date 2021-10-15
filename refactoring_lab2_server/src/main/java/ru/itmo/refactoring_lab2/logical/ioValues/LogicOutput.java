package ru.itmo.refactoring_lab2.logical.ioValues;


import ru.itmo.refactoring_lab2.logical.exceptions.ValueException;
import ru.itmo.refactoring_lab2.logical.ioValues.interfaces.IOElement;

public class LogicOutput implements IOElement {

    IOElement element;
    private final String name;
    public final static String COMMAND_NAME = "out";

    public LogicOutput(String name){
        this.name = name;
    }

    @Override
    public void setInput(Object input) {
        if (input instanceof IOElement) {
            this.element = (IOElement) input;
        }
    }

    @Override
    public String getCommandName() {
        return String.format("%s:%s", name, COMMAND_NAME);
    }

    @Override
    public String getFullName() {
        String input = " ";
        if (element != null){
            input = element.getFullName();
        }
        return String.format("%s(%s)", getCommandName(), input);
    }

    @Override
    public Boolean getOutput() throws ValueException {
        if (element != null) {
            return element.getOutput();
        } else {
            throw new ValueException("");
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
