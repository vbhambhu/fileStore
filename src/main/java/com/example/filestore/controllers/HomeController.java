package com.example.filestore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(Model model) {

        String jsFiles[] = {"ng-file-upload.min.js"};
        model.addAttribute("jsFiles", jsFiles);
        return "home";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public String uploadPage() {
        return "upload";
    }
}
