package com.ojt.service.statisticalService;

import com.ojt.model.dto.request.ProductStatistical;
import com.ojt.model.dto.request.StoreStatistical;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface StatisticalService {
    List<StoreStatistical> findAll();
    List<ProductStatistical> findAllProduct();

    void generateStoreStatisticalExcel(HttpServletResponse response);

    void generateProductStatisticalExcel(HttpServletResponse response);
}
