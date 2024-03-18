package com.ojt.service.OrderDetailService;

import com.ojt.model.entity.OrderDetails;
import com.ojt.model.entity.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetails> findAllByOrder (Orders orders);
    Page<OrderDetails> getAll (Orders orders, Integer pageNo);
    Page<OrderDetails> searchOrderDetail(String keyword, Integer pageNo);
}
