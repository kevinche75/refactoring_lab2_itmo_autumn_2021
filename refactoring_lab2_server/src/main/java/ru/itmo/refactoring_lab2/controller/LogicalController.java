package ru.itmo.refactoring_lab2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.itmo.refactoring_lab2.logical.exceptions.UnknownCommand;
import ru.itmo.refactoring_lab2.logical.exceptions.ValueException;
import ru.itmo.refactoring_lab2.logical.server.Server;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

@RestController
@RequestMapping("logical")
public class LogicalController {

    @Autowired
    Server server;

    HashMap<String, ArrayList<Integer>> commands;

    @PostConstruct
    public void init() {
        commands = new HashMap<>();
        commands.put("add", new ArrayList<>(Arrays. asList(2,2)));
        commands.put("connect", new ArrayList<>(Arrays. asList(2,3)));
        commands.put("set", new ArrayList<>(Arrays. asList(2,2)));
        commands.put("print", new ArrayList<>(Arrays. asList(0,0)));
        commands.put("show", new ArrayList<>(Arrays. asList(0,1)));
        commands.put("help", new ArrayList<>(Arrays. asList(0,0)));
    }

    @PostMapping("add/{element}/{name}")
    public String addLogicalElement(
            @PathVariable("element") String element,
            @PathVariable("name") String name
    ) throws ValueException, UnknownCommand {
        return server.parseAdd(element, name);
    }

    @PostMapping("commands")
    public HashMap<String, ArrayList<Integer>> getCommands(){
        return commands;
    }

    @PostMapping({"connect/{output}/{input}", "connect/{output}/{input}/{inputNumber}"})
    public String connectLogicalElements(
            @PathVariable("output") String output,
            @PathVariable("input") String input,
            @PathVariable(required = false) String inputNumber
    ) throws ValueException, UnknownCommand {
        return server.parseConnect(output, input, inputNumber);
    }

    @PostMapping("set/{name}/{value}")
    public String setLogicalElement(
            @PathVariable("name") String name,
            @PathVariable("value") String value
    ) throws ValueException, UnknownCommand {
        return server.parseSet(name, value);
    }

    @PostMapping("print")
    public String printLogicalElements() throws ValueException {
        return server.parsePrint();
    }

    @PostMapping({"show", "show/{elementName}"})
    public String showLogicalElement(
            @PathVariable(required = false) String elementName
    ) throws ValueException, UnknownCommand {
        return server.parseShow(elementName);
    }

    @PostMapping("help")
    public String getHelp(){
        return server.getHelp();
    }

    @ExceptionHandler(ValueException.class)
    public String handleValueException(ValueException e){
        return e.getMessage();
    }

    @ExceptionHandler(UnknownCommand.class)
    public String handleUnknownCommand(UnknownCommand e){
        return e.getMessage() + "\n" + server.getHelp();
    }
}
