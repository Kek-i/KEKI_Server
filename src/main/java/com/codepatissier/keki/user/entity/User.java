package com.codepatissier.keki.user.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.common.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(length = 30)
    private String nickname;

    @Column(nullable = false, length = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Provider provider;

    @Column(length = 300)
    private String profileImg;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String email, Provider provider, Role role) {
        this.email = email;
        this.provider = provider;
        this.role = role;
    }

    public void signup(String nickname, String refreshToken, Role role, String profileImg) {
        this.nickname = nickname;
        this.refreshToken = refreshToken;
        this.role = role;
        this.profileImg = profileImg;
    }

    public String getRole() {
        return this.role.getName();
    }

    public void storeSignUp(String nickname, String profileImg) {
        this.nickname = nickname;
        this.profileImg = profileImg;
    }
    
    public void modifyNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public void modifyProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}