package com.example.keki.dessert.entity;

import com.example.keki.common.BaseEntity;
import com.example.keki.common.Role;
import com.example.keki.user.entity.Store;
import com.example.keki.user.entity.User;
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
    private Store storeIdx;

    @Column(nullable = false, length = 100)
    private String dessertName;

    @Column(nullable = false)
    private int dessertPrice;

    @Column(nullable = false, length = 300)
    private String dessertDescription;

    @Builder
    public Dessert(Store storeIdx, String dessertName, int dessertPrice, String dessertDescription) {
        this.storeIdx = storeIdx;
        this.dessertName = dessertName;
        this.dessertPrice = dessertPrice;
        this.dessertDescription = dessertDescription;
    }
}
