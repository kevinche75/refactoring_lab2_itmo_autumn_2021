package ru.itmo.refactoring_lab2.logical.ioValues;

import ru.itmo.refactoring_lab2.logical.exceptions.ValueException;
import ru.itmo.refactoring_lab2.logical.ioValues.interfaces.IOElement;

public class LogicInput implements IOElement {

    private Boolean value;
    private final String name;
    public static final String COMMAND_NAME = "in";

    public LogicInput(String name){
        this.name = name;
    }

    @Override
    public void setInput(Object value) {
        if (value instanceof Boolean) {
            this.value = (Boolean) value;
        }
    }

    @Override
    public String getCommandName() {
        String boolString = " ";
        if (value != null){
            boolString = value.toString();
        }
        return String.format("%s:%s:%s", name, COMMAND_NAME, boolString);
    }

    @Override
    public String getFullName() {
        return getCommandName();
    }

    @Override
    public Boolean getOutput() throws ValueException {
        if (value != null){
            return value;
        } else {
            throw new ValueException("");
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
