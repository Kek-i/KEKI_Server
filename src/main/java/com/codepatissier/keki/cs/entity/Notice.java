package com.codepatissier.keki.cs.entity;

import com.codepatissier.keki.common.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Notice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeIdx;

    @Column(nullable = false, length = 50)
    private String noticeTitle;

    @Column(nullable = false, length = 500)
    private String noticeContent;

}
