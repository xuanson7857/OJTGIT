package com.ojt.service.ProductService;

import com.ojt.model.entity.Product;
import com.ojt.responsitoty.ProductRepository;
import com.ojt.service.LogImportService.LogImportService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private LogImportService logImportService;
    String line = "";
    @Transactional
    public boolean saveProductData(String fileName) {
        try {
            String pathFile = "/Users/son/Desktop/ok/ojt/src/main/resources/uploads/" + fileName;
            Path pathToFile = Paths.get(pathFile);
            BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8);
            int count = 1;
            Map<Integer,String> bugDetail = new HashMap<>();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Product product = new Product();
                product.setProductName(data[0]);
                String[] stringDate = data[1].split("/");
                String date = stringDate[2] + "-" + stringDate[1] + "-" + stringDate[0];
                product.setCreateDate(Date.valueOf(date));
                product.setPrice(Double.valueOf(data[2]));
                product.setQuantity(Integer.valueOf(data[3]));
                Product pro = productRepository.findProductByProductName(product.getProductName());
                switch (data[4]) {
                    case "create":
                        if (pro == null) {
                            productRepository.save(product);
                        }else{
                            bugDetail.put(count, "Sản phẩm đã tồn tại");
                        }
                        break;
                    case "delete":
                        if (pro != null) {
                            productRepository.delete(pro);
                        }else{
                            bugDetail.put(count, "Không tìm thấy sản phẩm cần xóa");
                        }
                        break;
                    case "update":
                        if (pro != null) {
                            product.setProductId(pro.getProductId());
                            productRepository.save(product);
                        }else{
                            bugDetail.put(count, "Không tìm thấy sản phẩm cần cập nhật");
                        }
                        break;
                    default:
                        bugDetail.put(count, "Lệnh thực thi không hợp lệ");
                        break;
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

    public void generateExcel(HttpServletResponse response) throws IOException {
        List<Product> products = productRepository.findAll();

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Customer Info");
        HSSFRow row = sheet.createRow(0);

        row.createCell(0).setCellValue("ID");
        row.createCell(1).setCellValue("Name");
        row.createCell(2).setCellValue("Time");
        row.createCell(3).setCellValue("Price");
        row.createCell(4).setCellValue("Quantity");

        int dataRowIndex = 1;

        for (Product product : products) {
            HSSFRow dataRow = sheet.createRow(dataRowIndex);

            dataRow.createCell(0).setCellValue(product.getProductId());
            dataRow.createCell(1).setCellValue(product.getProductName());
            dataRow.createCell(2).setCellValue(product.getCreateDate());
            dataRow.createCell(3).setCellValue(product.getPrice());
            dataRow.createCell(4).setCellValue(product.getQuantity());


            dataRowIndex++;
        }
        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Page<Product> getAll(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "productId");
        Pageable pageable = PageRequest.of(pageNo-1, 5, sort);
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchProduct(String keyword, Integer pageNo) {
//        // Tạo đối tượng Pageable với phân trang và sắp xếp theo thứ tự ID giảm dần
        Pageable pageable = PageRequest.of(pageNo-1, 5, Sort.by(Sort.Direction.DESC, "productId"));
        Page<Product> page = productRepository.findAllByProductNameContainingIgnoreCase(keyword, pageable);

        return page;
    }


}
