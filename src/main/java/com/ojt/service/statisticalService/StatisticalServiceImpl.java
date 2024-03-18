package com.ojt.service.statisticalService;

import com.ojt.model.dto.request.ProductStatistical;
import com.ojt.model.dto.request.StoreStatistical;
import com.ojt.model.entity.OrderDetails;
import com.ojt.model.entity.Orders;
import com.ojt.model.entity.Product;
import com.ojt.model.entity.Store;
import com.ojt.responsitoty.OrderDetailRepository;
import com.ojt.responsitoty.OrderRepository;
import com.ojt.responsitoty.ProductRepository;
import com.ojt.responsitoty.StoreRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service

public class StatisticalServiceImpl implements StatisticalService{
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderDetailRepository orderDetailRepository;
    @Autowired
    ProductRepository productRepository;
    @Override
    public List<StoreStatistical> findAll() {
        List<StoreStatistical> storeStatisticalList = new ArrayList<>();
        List<Store> storeList =  storeRepository.findAll();
        for (Store s:storeList) {
            List<Orders> ordersList=orderRepository.findAllByStore(s);
            StoreStatistical storeStatistical = new StoreStatistical();
            storeStatistical.setStoreId(s.getStoreId());
            storeStatistical.setStoreAddress(s.getCity()+", "+s.getDistrict()+", "+s.getStreet()+", "+s.getHomeNumber());
            storeStatistical.setOrderTotal(ordersList.size());
            double sum = 0;
            int count = 0;
            for (Orders o:ordersList) {
                for (OrderDetails od : o.getOrderDetails()){
                    sum += od.getPrice()*od.getQuantity();
                    count+=od.getQuantity();
                }
            }
            storeStatistical.setPriceTotal(sum);
            storeStatistical.setProductSellerTotal(count);
            storeStatisticalList.add(storeStatistical);
        }

        return storeStatisticalList;
    }

    @Override
    public List<ProductStatistical> findAllProduct() {
        List<ProductStatistical> productStatisticalList = new ArrayList<>();
        List<Product> productList = productRepository.findAll();

        for (Product p:productList) {
            ProductStatistical productStatistical = new ProductStatistical();
            productStatistical.setProductId(p.getProductId());
            productStatistical.setProductName(p.getProductName());
            double sum = 0;
            int count = 0;
            List<OrderDetails> orderDetailsList = orderDetailRepository.findAllByProduct(p);
            for (OrderDetails od : orderDetailsList){
                sum+= od.getPrice()*od.getQuantity();
                count+=od.getQuantity();
            }
            productStatistical.setProductSellTotal(count);
            productStatistical.setProductsPriceTotal(sum);
            productStatisticalList.add(productStatistical);
        }
        return productStatisticalList;
    }

    @Override
    public void generateStoreStatisticalExcel(HttpServletResponse response) {
        try{
            List<StoreStatistical> storeStatisticalList  = findAll();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Customer Info");
            HSSFRow row = sheet.createRow(0);

            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("StoreAddress");
            row.createCell(2).setCellValue("orderTotal");
            row.createCell(3).setCellValue("priceTotal");
            row.createCell(4).setCellValue("productSellerTotal");

            int dataRowIndex = 1;

            for (StoreStatistical storeStatistical : storeStatisticalList) {
                HSSFRow dataRow = sheet.createRow(dataRowIndex);

                dataRow.createCell(0).setCellValue(storeStatistical.getStoreId());
                dataRow.createCell(1).setCellValue(storeStatistical.getStoreAddress());
                dataRow.createCell(2).setCellValue(storeStatistical.getOrderTotal());
                dataRow.createCell(3).setCellValue(storeStatistical.getPriceTotal());
                dataRow.createCell(4).setCellValue(storeStatistical.getProductSellerTotal());

                dataRowIndex++;
            }
            ServletOutputStream ops = response.getOutputStream();
            workbook.write(ops);
            workbook.close();
            ops.close();
        }
        catch (Exception e){}

    }

    @Override
    public void generateProductStatisticalExcel(HttpServletResponse response) {
        try{
            List<ProductStatistical> productStatisticalList  = findAllProduct();
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet("Customer Info");
            HSSFRow row = sheet.createRow(0);

            row.createCell(0).setCellValue("ID");
            row.createCell(1).setCellValue("ProductName");
            row.createCell(2).setCellValue("ProductSellTotal");
            row.createCell(3).setCellValue("ProductsPriceTotal");


            int dataRowIndex = 1;

            for (ProductStatistical productStatistical : productStatisticalList) {
                HSSFRow dataRow = sheet.createRow(dataRowIndex);

                dataRow.createCell(0).setCellValue(productStatistical.getProductId());
                dataRow.createCell(1).setCellValue(productStatistical.getProductName());
                dataRow.createCell(2).setCellValue(productStatistical.getProductSellTotal());
                dataRow.createCell(3).setCellValue(productStatistical.getProductsPriceTotal());

                dataRowIndex++;
            }
            ServletOutputStream ops = response.getOutputStream();
            workbook.write(ops);
            workbook.close();
            ops.close();
        }
        catch (Exception e){}


    }
}
