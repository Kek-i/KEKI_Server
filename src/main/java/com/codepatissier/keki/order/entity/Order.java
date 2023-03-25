package com.codepatissier.keki.order.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.order.dto.OrderReq;
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
public class Order extends BaseEntity {
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

    @Column(nullable = false, length = 20)
    private String customerPhone;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(20) default 'ORDER_WAITING'")
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int extraPrice;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false, length = 500)
    private String request;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order", orphanRemoval = true)
    private List<OrderImg> images = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order", orphanRemoval = true)
    private List<OptionOrder> options = new ArrayList<>();

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

    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCELED;
    }

    public void changeOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public void editOrder(Dessert dessert, OrderReq orderReq){
        this.setDessert(dessert);
        this.setCustomerName(orderReq.getCustomerName());
        this.setCustomerPhone(orderReq.getCustomerPhone());
        this.setRequest(orderReq.getRequest());
        this.setPickupDate(orderReq.getPickupDate());
        this.setExtraPrice(orderReq.getExtraPrice());
        this.setTotalPrice(orderReq.getTotalPrice());
    }

    public void setDessert(Dessert dessert) {
        this.dessert = dessert;
    }

    public void setPickupDate(LocalDateTime pickupDate) {
        this.pickupDate = pickupDate;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public void setExtraPrice(int extraPrice) {
        this.extraPrice = extraPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setRequest(String request) {
        this.request = request;
    }
}
