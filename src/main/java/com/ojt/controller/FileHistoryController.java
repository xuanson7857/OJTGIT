package com.ojt.controller;

import com.ojt.service.LogImportService.LogImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FileHistoryController {
    @Autowired
    private LogImportService logImportService;

    @RequestMapping("/fileHistory")
    public String home (Model model) {
        model.addAttribute("log", logImportService.findAll());
        return "fileHistory/index";
    }

    @GetMapping("/logDetail/{id}")
    public String homeLogDetail (@PathVariable("id")Long id, Model model ) {
        model.addAttribute("detail", logImportService.findById(id));
        return "logDetail/index";

    }
}
