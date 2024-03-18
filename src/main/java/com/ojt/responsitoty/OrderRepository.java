package com.ojt.responsitoty;

import com.ojt.model.entity.Orders;
import com.ojt.model.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Timestamp;
import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    Orders findOrdersByCreateDateAndPhoneNumberAndStore(Timestamp cTimestamp, String cString, Store store);

    Page<Orders> findAllByPhoneNumberContainingIgnoreCase (String keyword, Pageable pageable);
    List<Orders> findAllByStore(Store store);

}
