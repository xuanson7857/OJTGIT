package com.ojt.controller;

import com.ojt.model.dto.request.SignInRequest;
import com.ojt.service.UserService.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {
    @Autowired
    HttpSession httpSession;
    @Autowired
    UserService userService;
    @GetMapping("/login")
    public String goLoginPage(Model model){
        model.addAttribute("user", new SignInRequest());
        return "login/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") SignInRequest user){
        if (userService.signIn(user)){
            return "redirect:/";
        }
        return "redirect:/login";
    }
    @RequestMapping("/abc")
    public String dddsa(){
        return "statistics/statistics-by-month";
    }
}
