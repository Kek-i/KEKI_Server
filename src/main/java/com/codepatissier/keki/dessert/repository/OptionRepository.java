package com.codepatissier.keki.dessert.repository;

import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.dessert.entity.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    List<Option> findByDessertAndStatusOrderByOptionIdx(Dessert dessert, String activeStatus);
}
