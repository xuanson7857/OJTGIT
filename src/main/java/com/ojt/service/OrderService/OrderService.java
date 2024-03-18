package com.ojt.service.OrderService;

import com.ojt.model.entity.Orders;
import org.springframework.data.domain.Page;

import java.util.List;

public interface OrderService {
    List<Orders> findAll();
    boolean saveOrdersData (String fileName);
    Orders findById (Long id);
    Page<Orders> getAll (Integer pageNo);
    Page<Orders> searchOrders(String keyword, Integer pageNo);
}
