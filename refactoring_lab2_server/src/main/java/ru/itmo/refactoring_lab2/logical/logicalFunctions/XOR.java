package ru.itmo.refactoring_lab2.logical.logicalFunctions;


import ru.itmo.refactoring_lab2.logical.exceptions.ValueException;

public class XOR extends TwoParamsElement {

    public final static String COMMAND_NAME = "xor";
    public XOR(String name) {
        super(name);
    }

    @Override
    public Boolean getOutput() throws ValueException {
        if (firstInput == null){
            throw new ValueException("");
        }
        if (secondInput == null){
            throw new ValueException("");
        }
        return firstInput.getOutput() ^ secondInput.getOutput();
    }

    @Override
    public String getCommandName() {
        return String.format("%s:%s", name, COMMAND_NAME);
    }
}
