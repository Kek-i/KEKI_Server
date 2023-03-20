package com.codepatissier.keki.dessert.repository;

import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DessertRepository extends JpaRepository<Dessert, Long> {
    Page<Dessert> findByStoreAndStatusOrderByDessertIdxDesc(Store store, String activeStatus, Pageable page);
    List<Dessert> findByStoreAndStatusOrderByDessertIdx(Store store, String activeStatus);
    Page<Dessert> findByStoreAndStatusAndDessertIdxLessThanOrderByDessertIdxDesc(Store store, String activeStatus, Long cursorIdx, Pageable page);
    boolean existsByStatusAndDessertIdxLessThan(String activeStatus, Long dessertIdx);
    Optional<Dessert> findByDessertIdxAndStatus(Long dessertIdx, String activeStatus);
    void deleteByStore(Store store);
}

