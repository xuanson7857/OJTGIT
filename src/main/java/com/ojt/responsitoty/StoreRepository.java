package com.ojt.responsitoty;

import com.ojt.model.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findStoreByCityAndDistrictAndStreetAndHomeNumberEqualsIgnoreCase (String city, String district, String street, String homeNumber);
    Page<Store> findStoreByCityContainingIgnoreCase (String keyword, Pageable pageable);
}
