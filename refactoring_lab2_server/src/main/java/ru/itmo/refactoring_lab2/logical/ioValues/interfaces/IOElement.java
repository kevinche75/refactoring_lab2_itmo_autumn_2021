package ru.itmo.refactoring_lab2.logical.ioValues.interfaces;

public interface IOElement extends NamedElement, Output{

    void setInput(Object input);
    String getCommandName();
    String getFullName();
}
