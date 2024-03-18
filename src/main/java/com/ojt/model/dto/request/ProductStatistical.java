package com.ojt.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class ProductStatistical {
    private Long productId;
    private String productName;
    private int productSellTotal;
    private double productsPriceTotal;
}
