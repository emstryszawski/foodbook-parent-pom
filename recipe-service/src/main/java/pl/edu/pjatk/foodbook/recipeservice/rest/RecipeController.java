package pl.edu.pjatk.foodbook.recipeservice.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class RecipeController {

    @GetMapping
    public String hello() {
        return "Hello Recipe!";
    }
}
