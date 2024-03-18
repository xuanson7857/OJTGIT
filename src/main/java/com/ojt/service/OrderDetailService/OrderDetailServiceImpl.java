package com.ojt.service.OrderDetailService;

import com.ojt.model.entity.OrderDetails;
import com.ojt.model.entity.Orders;
import com.ojt.responsitoty.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Override
    public List<OrderDetails> findAllByOrder(Orders orders) {
        return orderDetailRepository.findAllByOrders(orders);
    }

    @Override
    public Page<OrderDetails> getAll(Orders orders, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5);
        return orderDetailRepository.findAllByOrders(orders,pageable);
    }

    @Override
    public Page<OrderDetails> searchOrderDetail(String keyword, Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo - 1, 5);
        return orderDetailRepository.findAllByProduct_ProductName(keyword, pageable);
    }
}
