package com.ojt.service.StoreService;

import com.ojt.model.entity.Store;
import com.ojt.responsitoty.StoreRepository;
import com.ojt.service.LogImportService.LogImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
public class StoreServiceImpl implements StoreService{
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private LogImportService logImportService;
    String line = "";
    @Override
    public boolean saveStoreData(String fileName) {
        try {
            String pathFile = "/Users/son/Desktop/ok/ojt/src/main/resources/uploads/" + fileName;
            Path pathToFile = Paths.get(pathFile);
            BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.UTF_8);
            int count = 1;
            Map<Integer,String> bugDetail = new HashMap<>();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Store store = new Store();
                store.setHomeNumber(data[0].replaceAll("\"", "").trim());
                store.setStreet(data[1].replaceAll("\"", "").trim());
                store.setDistrict(data[2].replaceAll("\"", "").trim());
                store.setCity(data[3].replaceAll("\"", "").trim());
                String[] stringDate = data[4].split("/");
                String date = stringDate[2] + "-" + stringDate[1] + "-" + stringDate[0];
                store.setCreateDate(Date.valueOf(date));

                Store sto = storeRepository.findStoreByCityAndDistrictAndStreetAndHomeNumberEqualsIgnoreCase(store.getCity(), store.getDistrict(), store.getStreet(), store.getHomeNumber());
                switch (data[5]) {
                    case "create":
                        if (sto == null) {
                            storeRepository.save(store);
                        }else{
                            bugDetail.put(count, "Cửa hàng đã tồn tại ở địa chỉ này!!!");
                        }
                        break;
                    case "delete":
                        if (sto != null) {
                            storeRepository.delete(sto);
                        }else{
                            bugDetail.put(count, "Không tìm thấy cửa hàng cần xóa");
                        }
                        break;
                    case "update":
                        if (sto != null) {
                            store.setStoreId(sto.getStoreId());
                            storeRepository.save(store);
                        }else{
                            bugDetail.put(count, "Không tìm thấy cửa hàng cần cập nhật");
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
        } catch (Exception e) {
            System.out.println(1);
            return false;
        }
    }


    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Override
    public Store findByAddress(Store store) {
        return storeRepository.findStoreByCityAndDistrictAndStreetAndHomeNumberEqualsIgnoreCase(store.getCity(), store.getDistrict(), store.getStreet(), store.getHomeNumber());
    }

    @Override
    public Page<Store> getAll(Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "storeId");
        Pageable pageable = PageRequest.of(pageNo - 1, 5, sort);
        return storeRepository.findAll(pageable);
    }

    @Override
    public Page<Store> searchStore(String keyword, Integer pageNo) {
        Sort sort = Sort.by(Sort.Direction.DESC, "storeId");
        Pageable pageable = PageRequest.of(pageNo - 1, 5, sort);
        Page<Store> page = storeRepository.findStoreByCityContainingIgnoreCase(keyword, pageable);
        return page;
    }
}
