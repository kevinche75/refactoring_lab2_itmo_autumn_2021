package ru.itmo.refactoring_lab2.controller;

import org.springframework.web.bind.annotation.*;
import ru.itmo.refactoring_lab2.logical.server.Server;

@RestController
@RequestMapping("logical")
public class LogicalController {

    Server server = new Server();

    @PostMapping("command")
    public String executeCommand(@RequestBody String strCommand){
        return server.parseCommand(strCommand);
    }
}
