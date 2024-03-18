package com.ojt.responsitoty;

import com.ojt.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query("SELECT p FROM Product p WHERE p.productName LIKE %?1%")
    List<Product> searchProduct(String keyword);
    Page<Product> findAllByProductNameContainingIgnoreCase(String keyword, Pageable pageable);
    Product findProductByProductName(String productName);
}
