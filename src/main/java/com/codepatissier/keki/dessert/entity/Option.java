package com.codepatissier.keki.dessert.entity;

import com.codepatissier.keki.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Table(name = "options")
@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Option extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "dessertIdx")
    private Dessert dessert;

    @Column(nullable = false, length = 100)
    private String description;

    @Column(nullable = false)
    private Integer price;
}
