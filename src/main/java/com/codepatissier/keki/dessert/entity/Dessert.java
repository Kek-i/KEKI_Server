package com.codepatissier.keki.dessert.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.store.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Dessert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dessertIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "storeIdx")
    private Store store;

    @Column(nullable = false, length = 100)
    private String dessertName;

    @Column(nullable = false)
    private int dessertPrice;

    @Column(nullable = false, length = 300)
    private String dessertDescription;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "dessert", orphanRemoval = true)
    private DessertImg image;

    @Builder
    public Dessert(Store store, String dessertName, int dessertPrice, String dessertDescription, DessertImg image) {
        this.store = store;
        this.dessertName = dessertName;
        this.dessertPrice = dessertPrice;
        this.dessertDescription = dessertDescription;
        this.image = image;
    }
}
