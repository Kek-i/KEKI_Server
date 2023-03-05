package com.codepatissier.keki.order.entity;

import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.post.entity.OrderStatus;
import com.codepatissier.keki.store.entity.Store;
import com.codepatissier.keki.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "orders")
@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "storeIdx")
    private Store store;

    @ManyToOne
    @JoinColumn(nullable = false, name = "dessertIdx")
    private Dessert dessert;

    @Column(nullable = false)
    private LocalDateTime pickupDate;

    @Column(nullable = false, length = 30)
    private String customerName;

    @Column(nullable = false, length = 12)
    private String customerPhone;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'order_waiting'")
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int extraPrice;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false, length = 500)
    private String request;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", orphanRemoval = true)
    private List<OptionOrder> options = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true)
    private List<OrderImg> imgs = new ArrayList<>();

    @Builder
    public Order(User user, Store store, Dessert dessert, LocalDateTime pickupDate, String customerName, String customerPhone, int extraPrice, int totalPrice, String request) {
        this.user = user;
        this.store = store;
        this.dessert = dessert;
        this.pickupDate = pickupDate;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.extraPrice = extraPrice;
        this.totalPrice = totalPrice;
        this.request = request;
    }

}
