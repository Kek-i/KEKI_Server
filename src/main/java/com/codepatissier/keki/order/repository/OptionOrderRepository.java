package com.codepatissier.keki.order.repository;

import com.codepatissier.keki.order.entity.OptionOrder;
import com.codepatissier.keki.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionOrderRepository extends JpaRepository<OptionOrder, Long> {
    List<OptionOrder> findByOrderAndStatusEquals(Order order, String activeStatus);
}
