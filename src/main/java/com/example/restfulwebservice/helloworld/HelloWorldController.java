package com.example.restfulwebservice.helloworld;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
public class HelloWorldController {

    private MessageSource source;

    public HelloWorldController(MessageSource source) {
        this.source = source;
    }

    @GetMapping("/hello-world")
    public String helloWorld () {
        return "Hello World";
    }

    @GetMapping("/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean (@PathVariable String name) {
        return new HelloWorldBean(String.format("Hello World , %s",name));
    }

    @GetMapping(path = "/hello-world-internationalized")
    public String hellWorldInternationalized(@RequestHeader(name = "Accept-Language",required=false) Locale locale) {
        log.info("country {} ",locale.getCountry());
        return source.getMessage("greeting.message",null,locale);
    }
}
