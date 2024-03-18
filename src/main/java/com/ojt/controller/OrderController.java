package com.ojt.controller;

import com.ojt.model.entity.OrderDetails;
import com.ojt.model.entity.Orders;
import com.ojt.service.OrderDetailService.OrderDetailService;
import com.ojt.service.OrderService.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    HttpSession httpSession;
    @RequestMapping("/order")
    public String home (Model model, @Param("keyword") String keyword,
                        @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        if (httpSession.getAttribute("userLogin") == null){
            return "redirect:/login";
        }
        Page<Orders> orders = orderService.getAll(pageNo);
        if (keyword != null) {
            orders = orderService.searchOrders(keyword, pageNo);
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("totalPage", orders.getTotalPages());
        model.addAttribute("currentPage", pageNo);

        model.addAttribute("check", true);
        model.addAttribute("orders", orders);
        return "order/index";
    }
    @PostMapping("/uploadOrderFile")
    public String uploadOrderFile(@RequestParam("orderFile") MultipartFile file, Model model){
        String fileName = file.getOriginalFilename();
        if (fileName.contains(".csv")){
            try {
                file.transferTo(new File("/Users/son/Desktop/ok/ojt/src/main/resources/uploads/" + fileName));
                if (orderService.saveOrdersData(fileName)){
                    return "redirect:/order";
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        model.addAttribute("check", true);
        model.addAttribute("orders", orderService.findAll());
        return "order/index";
    }

    @GetMapping("/orderDetail/{id}")
    public String orderDetail(@PathVariable("id") Long id, Model model,
                              @Param("keyword") String keyword,
                              @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo) {
        Orders orders = orderService.findById(id);

        Page<OrderDetails> orderDetails = orderDetailService.getAll(orders, pageNo);
        if (keyword != null) {
            orderDetails = orderDetailService.searchOrderDetail(keyword, pageNo);
        }

        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPage", orderDetails.getTotalPages());

        model.addAttribute("orderDetail", orderDetails);
        return "orderDetail/index";
    }
}
