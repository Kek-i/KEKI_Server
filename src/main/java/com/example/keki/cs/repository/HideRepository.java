package com.example.keki.cs.repository;

import com.example.keki.cs.entity.Hide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HideRepository extends JpaRepository<Hide, Long> {

}
