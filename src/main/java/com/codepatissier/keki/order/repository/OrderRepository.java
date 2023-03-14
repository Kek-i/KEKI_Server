package com.codepatissier.keki.order.repository;

import com.codepatissier.keki.order.entity.Order;
import com.codepatissier.keki.order.entity.OrderStatus;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<List<Order>> findAllByUserAndOrderStatusEqualsOrderByPickupDateDesc(User user, OrderStatus orderStatus);
    Optional<List<Order>> findAllByUserOrderByPickupDateDesc(User user);
    Optional<List<Order>> findAllByStore_UserOrderByPickupDateDesc(User user);
    Optional<List<Order>> findAllByStore_UserAndOrderStatusEqualsOrderByPickupDateDesc(User user, OrderStatus orderStatus);
    int countByUserAndOrderStatusEquals(User user, OrderStatus orderWaiting);
    int countAllByUser(User user);
    int countByStore_UserAndOrderStatusEquals(User user, OrderStatus orderWaiting);
    int countAllByStore_User(User user);
}
