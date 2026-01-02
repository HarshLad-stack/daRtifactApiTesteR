package com.example.dartifactapitester.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class FrontendController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("result","");
        return "index"; // served from static/
    }
}
