package ru.itmo.refactoring_lab2.logical.logicalFunctions;


import ru.itmo.refactoring_lab2.logical.exceptions.ValueException;
import ru.itmo.refactoring_lab2.logical.ioValues.interfaces.IOElement;

public abstract class TwoParamsElement implements IOElement {

    IOElement firstInput;
    IOElement secondInput;
    final String name;
    public final static String COMMAND_NAME = "";

    public TwoParamsElement(String name){
        this.name = name;
    }

    @Override
    public void setInput(Object value) {
        if (value instanceof IOElement) {
            if (firstInput == null) {
                firstInput = (IOElement) value;
            } else {
                secondInput = (IOElement) value;
            }
        }
    }

    public void setFirstInput(IOElement value){
        firstInput = value;
    }

    public void setSecondInput(IOElement value){
        secondInput = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCommandName() {
        return String.format("%s:%s", name, COMMAND_NAME);
    }

    @Override
    public abstract Boolean getOutput() throws ValueException;

    @Override
    public String getFullName() {
        String firstStringInput = " ";
        String secondStringInput = " ";
        if (firstInput != null){
            firstStringInput = firstInput.getFullName();
        }
        if (secondInput != null){
            secondStringInput = secondInput.getFullName();
        }
        return String.format("%s(%s,%s)", getCommandName(), firstStringInput, secondStringInput);
    }


}
