package com.codepatissier.keki.order.repository;

import com.codepatissier.keki.dessert.entity.Option;
import com.codepatissier.keki.order.entity.OptionOrder;
import com.codepatissier.keki.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionOrderRepository extends JpaRepository<OptionOrder, Long> {
    List<OptionOrder> findByOrderAndStatusEquals(Order order, String activeStatus);
    OptionOrder findByOrderAndOption(Order order, Option option);
}
