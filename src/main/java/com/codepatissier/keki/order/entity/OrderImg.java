package com.codepatissier.keki.order.entity;

import com.codepatissier.keki.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class OrderImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderImgIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "orderIdx")
    private Order order;

    @Column(nullable = false, length = 300)
    private String imgUrl;

    @Builder
    public OrderImg(Order order, String imgUrl){
        this.order = order;
        this.imgUrl = imgUrl;
    }
}
