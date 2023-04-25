package pl.edu.pjatk.foodbook.foodbookservice.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class FoodbookController {

    @GetMapping
    public String hello() {
        return "Hello Foodbook!";
    }
}
