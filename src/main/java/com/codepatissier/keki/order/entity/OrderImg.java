package com.codepatissier.keki.order.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class OrderImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderImgIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "orderIdx")
    private Order order;

    @Column(nullable = false, length = 300)
    private String imgUrl;
}
