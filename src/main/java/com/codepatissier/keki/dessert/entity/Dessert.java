package com.codepatissier.keki.dessert.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.store.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Dessert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dessertIdx;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "storeIdx")
    private Store store;

    @NotNull
    @Column(length = 100)
    private String dessertName;

    @NotNull
    @Column
    private int dessertPrice;

    @NotNull
    @Column(length = 300)
    private String dessertDescription;

    @NotNull
    @Column(length = 300)
    private String dessertImg;

    @Builder
    public Dessert(Store store, String dessertName, int dessertPrice, String dessertDescription, String dessertImg) {
        this.store = store;
        this.dessertName = dessertName;
        this.dessertPrice = dessertPrice;
        this.dessertDescription = dessertDescription;
        this.dessertImg = dessertImg;
    }
}
