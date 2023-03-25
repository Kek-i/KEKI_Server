package com.codepatissier.keki.store.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.common.entityListener.StoreEntityListener;
import com.codepatissier.keki.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
@SQLDelete(sql = "UPDATE store SET status = 'inactive', last_modified_date = current_timestamp WHERE store_idx = ?")
@EntityListeners(StoreEntityListener.class)
public class Store extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeIdx;

    @OneToOne
    @JoinColumn(name = "userIdx")
    private User user;

    @Column(length = 100)
    private String address;

    @Column(length = 200)
    private String introduction;

    @Column(length = 50)
    private String accountHolder;

    @Column(length = 100)
    private String accountNumber;

    /**
     * 사업자 정보
     */
    // 대표자명
    @Column(length = 50)
    private String businessName;

    // 상호명
    @Column(length = 50)
    private String brandName;

    // 사업자 주소
    @Column(length = 100)
    private String businessAddress;

    // 사업자 등록번호
    @Column(length = 100)
    private String businessNumber;

    @Builder
    public Store(User user, String address, String introduction, String accountHolder, String accountNumber, String businessName, String brandName, String businessAddress, String businessNumber) {
        this.user = user;
        this.address = address;
        this.introduction = introduction;
        this.accountHolder = accountHolder;
        this.accountNumber = accountNumber;
        this.businessName = businessName;
        this.brandName = brandName;
        this.businessAddress = businessAddress;
        this.businessNumber = businessNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setBusinessName(String businessName) { this.businessName = businessName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public void setBusinessAddress(String businessAddress) { this.businessAddress = businessAddress; }
    public void setBusinessNumber(String businessNumber) { this.businessNumber = businessNumber; }
}
