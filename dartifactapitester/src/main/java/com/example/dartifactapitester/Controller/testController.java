package com.example.dartifactapitester.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class testController {
    @GetMapping("/works")
    public String ping(){
        return "Court is in Session";
    }
}
