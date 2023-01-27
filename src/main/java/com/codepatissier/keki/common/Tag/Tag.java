package com.codepatissier.keki.common.Tag;

import com.codepatissier.keki.common.BaseEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Tag extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagIdx;

    @Getter
    @Column(nullable = false, length = 50)
    private String tagName;

    @Builder
    public Tag(String tagName) {
        this.tagName = tagName;
    }

}
