package com.ojt.controller;

import com.ojt.model.dto.request.StoreStatistical;
import com.ojt.service.statisticalService.StatisticalService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
public class HomeController {
    @Autowired
    HttpSession httpSession;
    @Autowired
    StatisticalService statisticalService;
    @GetMapping("/")
    public String home(Model model){
        if (httpSession.getAttribute("userLogin") == null){
            return "redirect:/login";
        }

        model.addAttribute("storeList", statisticalService.findAll());
        return "home/index";
    }
    @GetMapping("/statisticalProduct")
    public  String statistical(Model model){
        if (httpSession.getAttribute("userLogin") == null){
            return "redirect:/login";
        }
        model.addAttribute("productList",statisticalService.findAllProduct());
        return "home/product-statical";

    }

    @GetMapping("/storeStatisticalExport")
    public void exportExcel (HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=storeStatistical.xls";

        response.setHeader(headerKey, headerValue);
        statisticalService.generateStoreStatisticalExcel(response);

    }
    @GetMapping("/productStatisticalExport")
    public void productExportExcel (HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=storeStatistical.xls";

        response.setHeader(headerKey, headerValue);
        statisticalService.generateProductStatisticalExcel(response);
    }

}
