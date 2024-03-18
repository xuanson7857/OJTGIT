package com.ojt.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StoreStatistical {
    private Long storeId;
    private String storeAddress;
    private int orderTotal;
    private double priceTotal;
    private int productSellerTotal;
}
