package ru.itmo.refactoring_lab2.logical.ioValues.interfaces;

import java.io.Serializable;

public interface IOElement extends NamedElement, Output, Serializable {

    void setInput(Object input);
    String getCommandName();
    String getFullName();
}
