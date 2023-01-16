package com.codepatissier.keki.dessert.repository;

import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.store.entity.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DessertRepository extends JpaRepository<Dessert, Long> {
    List<Dessert> findByStoreOrderByDessertIdxDesc(Store store, Pageable page);
    List<Dessert> findByStoreAndDessertIdxLessThanOrderByDessertIdxDesc(Store store, Long cursorIdx, Pageable page);
    boolean existsByDessertIdxLessThan(Long dessertIdx);
}
