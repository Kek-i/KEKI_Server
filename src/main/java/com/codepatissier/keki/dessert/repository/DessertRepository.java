package com.codepatissier.keki.dessert.repository;

import com.codepatissier.keki.dessert.entity.Dessert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DessertRepository extends JpaRepository<Dessert, Long> {
}
