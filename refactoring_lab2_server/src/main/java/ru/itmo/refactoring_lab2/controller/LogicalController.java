package ru.itmo.refactoring_lab2.controller;

import org.springframework.web.bind.annotation.*;
import ru.itmo.refactoring_lab2.logical.exceptions.UnknownCommand;
import ru.itmo.refactoring_lab2.logical.exceptions.ValueException;
import ru.itmo.refactoring_lab2.logical.server.Server;

@RestController
@RequestMapping("logical")
public class LogicalController {

    Server server = new Server();

    @PostMapping("add/{element}/{name}")
    public String addLogicalElement(
            @PathVariable("element") String element,
            @PathVariable("name") String name
    ) throws ValueException, UnknownCommand {
        return server.parseAdd(element, name);
    }

    @PostMapping("connect/{output}/{input}")
    public String connectLogicalElements(
            @PathVariable("output") String output,
            @PathVariable("input") String input,
            @RequestParam(required = false) String inputNumber
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

    @PostMapping("show")
    public String showLogicalElement(
            @RequestParam(required = false) String elementName
    ) throws ValueException, UnknownCommand {
        return server.parseShow(elementName);
    }

    @GetMapping("help")
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
