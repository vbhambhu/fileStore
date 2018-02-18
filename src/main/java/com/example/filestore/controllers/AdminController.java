package com.example.filestore.controllers;

import com.example.filestore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {


    @Autowired
    UserService userService;

    @RequestMapping(value = "/admin/users", method = RequestMethod.GET)
    public String userList(Model model) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();
        System.out.println(userDetails.getAuthorities());
        System.out.println(userDetails.getUsername());
        System.out.println(userDetails.isEnabled());

        model.addAttribute("users", userService.findAll());
        String jsFiles[] = {"datatables.min.js"};
        model.addAttribute("jsFiles", jsFiles);
        return "admin/users/list";
    }


    @RequestMapping(value = "/admin/user/edit", method = RequestMethod.GET)
    public String editUser(@RequestParam(required = false) String id, Model model) {

        if(id == null){
            return "admin/users/list";
        }

        //model.addAttribute("groups", userService.getAllGroups());

        model.addAttribute("user", userService.getUserById(Long.valueOf(id)));
        return "admin/users/edit";
    }
}
