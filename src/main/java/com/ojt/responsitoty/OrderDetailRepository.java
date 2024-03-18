package com.ojt.responsitoty;

import com.ojt.model.entity.OrderDetails;
import com.ojt.model.entity.Orders;
import com.ojt.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetails, Long> {
    List<OrderDetails> findAllByOrders (Orders orders);
    Page<OrderDetails> findAllByOrders (Orders orders, Pageable pageable);
     Page<OrderDetails> findAllByProduct_ProductName (String keyword, Pageable pageable);
     List<OrderDetails> findAllByProduct(Product product);

}
