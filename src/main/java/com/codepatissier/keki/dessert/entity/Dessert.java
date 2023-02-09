package com.codepatissier.keki.dessert.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.common.entityListener.DessertEntityListener;
import com.codepatissier.keki.store.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
@SQLDelete(sql = "UPDATE dessert SET status = 'inactive' WHERE dessert_idx = ?")
@EntityListeners(DessertEntityListener.class)
public class Dessert extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dessertIdx;

    @NotBlank
    @ManyToOne
    @JoinColumn(name = "storeIdx")
    private Store store;

    @NotBlank
    @Column(length = 100)
    private String dessertName;

    @NotBlank
    @Column
    private Integer dessertPrice;

    @NotBlank
    @Column(length = 300)
    private String dessertDescription;

    @NotBlank
    @Column(length = 300)
    private String dessertImg;

    @Builder
    public Dessert(Store store, String dessertName, Integer dessertPrice, String dessertDescription, String dessertImg) {
        this.store = store;
        this.dessertName = dessertName;
        this.dessertPrice = dessertPrice;
        this.dessertDescription = dessertDescription;
        this.dessertImg = dessertImg;
    }

    public void setDessertImg(String dessertImg) { this.dessertImg = dessertImg; }

    public void setDessertName(String dessertName) {
        this.dessertName = dessertName;
    }

    public void setDessertPrice(Integer dessertPrice) {
        this.dessertPrice = dessertPrice;
    }

    public void setDessertDescription(String dessertDescription) {
        this.dessertDescription = dessertDescription;
    }
}
