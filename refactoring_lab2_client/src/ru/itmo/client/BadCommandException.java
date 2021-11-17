package ru.itmo.client;

public class BadCommandException extends Exception{
    public BadCommandException(String message){
        super(message);
    }
}
