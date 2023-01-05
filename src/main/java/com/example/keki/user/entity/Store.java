package com.example.keki.user.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeIdx;

    @OneToOne
    @JoinColumn(name = "productIdx")
    private User userIdx;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(length = 200)
    private String introduction;

    @Column
    private String orderUrl;

    @Column(nullable = false, length = 50)
    private String businessName;

    @Column(nullable = false, length = 50)
    private String brandName;

    @Column(nullable = false, length = 100)
    private String businessAddress;

    @Column(nullable = false, length = 100)
    private String businessNumber;

    @Builder
    public Store(User userIdx, String address, String introduction, String orderUrl, String businessName, String brandName, String businessAddress, String businessNumber) {
        this.userIdx = userIdx;
        this.address = address;
        this.introduction = introduction;
        this.orderUrl = orderUrl;
        this.businessName = businessName;
        this.brandName = brandName;
        this.businessAddress = businessAddress;
        this.businessNumber = businessNumber;
    }
}
