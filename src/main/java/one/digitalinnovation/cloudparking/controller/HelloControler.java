package one.digitalinnovation.cloudparking.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class HelloControler {

    @GetMapping
    public String hello(){
        return "Hello Word. Java";
    }

}
