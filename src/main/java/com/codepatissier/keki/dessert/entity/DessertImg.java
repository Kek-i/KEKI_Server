package com.codepatissier.keki.dessert.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class DessertImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dessertImgIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "dessertIdx")
    private Dessert dessert;

    @Column(nullable = false, length = 300)
    private String imgUrl;

    @Builder
    public DessertImg(Dessert dessert, String imgUrl) {
        this.dessert = dessert;
        this.imgUrl = imgUrl;
    }
}
