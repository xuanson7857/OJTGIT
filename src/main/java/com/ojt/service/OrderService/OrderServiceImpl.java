package com.ojt.service.OrderService;

import com.ojt.model.entity.OrderDetails;
import com.ojt.model.entity.Orders;
import com.ojt.model.entity.Product;
import com.ojt.model.entity.Store;
import com.ojt.responsitoty.OrderDetailRepository;
import com.ojt.responsitoty.OrderRepository;
import com.ojt.responsitoty.ProductRepository;
import com.ojt.responsitoty.StoreRepository;
import com.ojt.service.LogImportService.LogImportService;
import com.ojt.service.ProductService.ProductService;
import com.ojt.service.StoreService.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private LogImportService logImportService;
    @Autowired
    private ProductRepository productRepository;
    @Override
    public List<Orders> findAll() {
        return orderRepository.findAll();
    }
    String line = "";

    @Override
    @Transactional
    public boolean saveOrdersData(String fileName) {
        try {
            String pathFile = "/Users/son/Desktop/ok/ojt/src/main/resources/uploads/" + fileName;
            Path pathToFile = Paths.get(pathFile);
            BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8);
            int count = 1;
            Map<Integer,String> bugDetail = new HashMap<>();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Orders orders = new Orders();
                System.out.println(data[4]);
                String d0 = data[0].replaceAll("\"", "").trim();
                String d1 = data[1].replaceAll("\"", "").trim();
                String d2 = data[2].replaceAll("\"", "").trim();
                String d3 = data[3].replaceAll("\"", "").trim();
                Store storeFind = storeRepository.findStoreByCityAndDistrictAndStreetAndHomeNumberEqualsIgnoreCase(d3,d2,d1,d0);
                orders.setStore(storeFind);
                String[] cutString= data[4].split("/");
                String[] newStringCut = cutString[2].split(" ");
                String dateString = newStringCut[0] + "-" + cutString[1] + "-" + cutString[0] + " " + newStringCut[1] + ":00";
                orders.setCreateDate(Timestamp.valueOf(dateString));
                orders.setPhoneNumber(data[5]);
                Orders ordersFind = orderRepository.findOrdersByCreateDateAndPhoneNumberAndStore(Timestamp.valueOf(dateString), data[5], storeFind);
                Orders odSave = null;
                switch (data[6]) {
                    case "create":
                            if (ordersFind == null){
                                odSave = orderRepository.save(orders);
                            }else {
                                orders.setId(ordersFind.getId());
                                odSave = orderRepository.save(orders);
                            }
                        break;
                    case "delete":
                        if (ordersFind != null) {
                            orderRepository.delete(ordersFind);
                        }else{
                            bugDetail.put(count, "Không tìm thấy sản phẩm cần xóa");
                        }
                        break;
                    case "update":
                        if (ordersFind != null) {
                            orders.setId(ordersFind.getId());
                            odSave = orderRepository.save(orders);
                        }else{
                            bugDetail.put(count, "Không tìm thấy sản phẩm cần cập nhật");
                        }
                        break;
                    default:
                        bugDetail.put(count, "Lệnh thực thi không hợp lệ");
                        break;
                }
                if (odSave != null){
                   List<OrderDetails> orderDetailsList = orderDetailRepository.findAllByOrders(odSave);
                   OrderDetails orderDetails = new OrderDetails();
                   Product product = productRepository.findProductByProductName(data[7]);
                   if (!orderDetailsList.isEmpty()){
                       for (OrderDetails od :orderDetailsList) {
                           if (od.getProduct().getProductId().equals(product.getProductId())){
                               orderDetails.setId(od.getId());
                               break;
                           }
                       }
                   }
                    orderDetails.setOrders(odSave);
                    orderDetails.setQuantity(Integer.valueOf(data[8]));
                    orderDetails.setPrice(Double.valueOf(data[9]));
                    orderDetails.setProduct(product);
                    orderDetailRepository.save(orderDetails);
                }
                count++;
            }
            if (!logImportService.getLog(bugDetail,fileName)){
                throw new Exception("Lỗi log");
            }
            File file = new File(pathFile);
            file.delete();
            return true;
        }catch (Exception e) {
            return false;
        }

    }

    @Override
    public Orders findById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    public Page<Orders> getAll(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo - 1, 5, sort);
        return orderRepository.findAll(pageable);
    }

    @Override
    public Page<Orders> searchOrders(String keyword, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(pageNo - 1, 5, sort);
        Page<Orders> page = orderRepository.findAllByPhoneNumberContainingIgnoreCase(keyword, pageable);
        return page;
    }
}
