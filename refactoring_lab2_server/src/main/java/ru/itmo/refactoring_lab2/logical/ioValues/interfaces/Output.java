package ru.itmo.refactoring_lab2.logical.ioValues.interfaces;


import ru.itmo.refactoring_lab2.logical.exceptions.ValueException;

public interface Output {
    Boolean getOutput() throws ValueException;
}
