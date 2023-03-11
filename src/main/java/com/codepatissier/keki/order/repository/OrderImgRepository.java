package com.codepatissier.keki.order.repository;

import com.codepatissier.keki.order.entity.Order;
import com.codepatissier.keki.order.entity.OrderImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderImgRepository extends JpaRepository<OrderImg, Long> {
    List<OrderImg> findByOrderAndStatusEquals(Order order, String activeStatus);
}
