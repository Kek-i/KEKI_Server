package com.codepatissier.keki.user.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.common.Constant;
import com.codepatissier.keki.common.Role;
import com.codepatissier.keki.common.entityListener.UserEntityListener;
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
@SQLDelete(sql = "UPDATE user SET status = 'inactive', last_modified_date = current_timestamp WHERE user_idx = ?")
@EntityListeners(UserEntityListener.class)
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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Builder
    public User(String email, Provider provider, Role role) {
        this.email = email;
        this.provider = provider;
        this.role = role;
    }

    public void signup(String nickname, Role role, String profileImg) {
        this.nickname = nickname;
        this.role = role;
        this.profileImg = profileImg;
    }

    public String getRole() {
        return this.role.getName();
    }

    public void storeSignUp(String nickname, String profileImg, Role role) {
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.role = role;
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
    public void setRole(Role role) {this.role = role;}

    // 회원 탈퇴
    public void signout() {
        this.setNickname("알 수 없음");
        this.setProfileImg(null);
        this.setRole(Role.ANONYMOUS);
        this.setStatus(Constant.INACTIVE_STATUS);
    }

    // 회원 로그아웃
    public void logout() {
        this.setStatus(Constant.LOGOUT_STATUS);
    }

    public void login() {
        this.setStatus(Constant.ACTIVE_STATUS);
    }
}