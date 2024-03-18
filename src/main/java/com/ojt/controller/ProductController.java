package com.ojt.controller;

import com.ojt.model.entity.Product;
import com.ojt.service.ProductService.ProductServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;
    @Autowired
    HttpSession httpSession;
    @RequestMapping("/product")
    public String homeProduct (Model model, @Param("keyword") String keyword,
                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (httpSession.getAttribute("userLogin") == null){
            return "redirect:/login";
        }
        Page<Product> products = productService.getAll(pageNo);
        if (keyword != null) {
            products = productService.searchProduct(keyword, pageNo);
        }
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPage", products.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        model.addAttribute( "check", false);
        model.addAttribute("products", products);

        return "product/index";
    }


    @GetMapping("/export")
    public void exportExcel (HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Product.xls";

        response.setHeader(headerKey, headerValue);
        productService.generateExcel(response);
    }
    @PostMapping("/uploadProductFile")
    public String upload(@RequestParam("productFile")MultipartFile file, Model model){
        String fileName = file.getOriginalFilename();
        if (fileName.contains(".csv")){
            try {
                file.transferTo(new File("/Users/son/Desktop/ok/ojt/src/main/resources/uploads/" + fileName));
                if (productService.saveProductData(fileName)){
                    return "redirect:/product";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        model.addAttribute("check", true);
        model.addAttribute("products", productService.findAll());
        return "product/index";
    }


}
