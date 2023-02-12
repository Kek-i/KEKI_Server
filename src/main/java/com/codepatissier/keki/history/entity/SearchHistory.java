package com.codepatissier.keki.history.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@DynamicInsert
@Getter
public class SearchHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long searchHistoryIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "userIdx")
    private User user;

    @Column(nullable = false, length = 100)
    private String searchWord;

    @Builder
    public SearchHistory(User user, String searchWord) {
        this.user = user;
        this.searchWord = searchWord;
    }
}
