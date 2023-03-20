package com.codepatissier.keki.order.entity;

import com.codepatissier.keki.dessert.entity.Option;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class OptionOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionOrderIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "orderIdx")
    private Order order;

    @ManyToOne
    @JoinColumn(nullable = false, name = "optionIdx")
    private Option option;
}
