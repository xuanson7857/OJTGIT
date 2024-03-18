package com.ojt.service.StoreService;

import com.ojt.model.entity.Store;
import org.springframework.data.domain.Page;

import java.util.List;

public interface StoreService {
    boolean saveStoreData (String fileName);
    List<Store> findAll ();
    Store findByAddress(Store store);
    Page<Store> getAll (Integer pageNo);
    Page<Store> searchStore(String keyword, Integer pageNo);
}
