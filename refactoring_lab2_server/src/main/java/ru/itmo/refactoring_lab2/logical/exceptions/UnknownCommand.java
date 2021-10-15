package ru.itmo.refactoring_lab2.logical.exceptions;

public class UnknownCommand extends  Exception{
    public UnknownCommand(String message){
        super(message);
    }
}
