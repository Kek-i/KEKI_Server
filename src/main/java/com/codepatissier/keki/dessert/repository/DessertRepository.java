package com.codepatissier.keki.dessert.repository;

import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DessertRepository extends JpaRepository<Dessert, Long> {
    Page<Dessert> findByStoreOrderByDessertIdxDesc(Store store, Pageable page);
    Page<Dessert> findByStoreAndDessertIdxLessThanOrderByDessertIdxDesc(Store store, Long cursorIdx, Pageable page);
    boolean existsByDessertIdxLessThan(Long dessertIdx);
}

