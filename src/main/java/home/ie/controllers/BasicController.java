package home.ie.controllers;

import home.ie.beans.WelcomeBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BasicController {

    @GetMapping("/welcome")
    public String welcome() {
        return "Hello World!";
    }

    @GetMapping("/welcome-with-object")
    public WelcomeBean welcomeWithObject() {
        String temp = "dd" + "dd";
        return new WelcomeBean("Hello World !");
    }

    @GetMapping("/welcome-with-parameter/name/{name}")
    public WelcomeBean welcomeWithParameter(@PathVariable String name) {

        return new WelcomeBean(String.format("Hello World, %s!!!", name));

    }
}
