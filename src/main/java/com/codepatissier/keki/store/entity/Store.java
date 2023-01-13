package com.codepatissier.keki.store.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeIdx;

    @OneToOne
    @JoinColumn(name = "userIdx")
    private User user;

    @Column(nullable = false, length = 100)
    private String address;

    @Column(length = 200)
    private String introduction;

    @Column
    private String orderUrl;

    /**
     * 사업자 정보
     */
    // 대표자명
    @Column(nullable = false, length = 50)
    private String businessName;

    // 가게 이름
    @Column(nullable = false, length = 50)
    private String brandName;

    // 사업자 주소
    @Column(nullable = false, length = 100)
    private String businessAddress;

    // 사업자 등록번호
    @Column(nullable = false, length = 100)
    private String businessNumber;

    @Builder
    public Store(User user, String address, String introduction, String orderUrl, String businessName, String brandName, String businessAddress, String businessNumber) {
        this.user = user;
        this.address = address;
        this.introduction = introduction;
        this.orderUrl = orderUrl;
        this.businessName = businessName;
        this.brandName = brandName;
        this.businessAddress = businessAddress;
        this.businessNumber = businessNumber;
    }
}