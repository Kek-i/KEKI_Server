package com.codepatissier.keki.store.repository;

import com.codepatissier.keki.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    List<Store> findByStoreIdx(Long storeIdx);
}
