package com.codepatissier.keki.post.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.store.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
public class Post extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postIdx;

    @ManyToOne
    @JoinColumn(nullable = false, name = "dessertIdx")
    private Dessert dessert;

    @ManyToOne
    @JoinColumn(nullable = false, name = "storeIdx")
    private Store store;

    @Column(nullable = false, length = 300)
    private String postDescription;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "post", orphanRemoval = true)
    private List<PostImg> images = new ArrayList<>();

    @Builder
    public Post(Dessert dessert, Store store, String postDescription, List<PostImg> images) {
        this.dessert = dessert;
        this.store = store;
        this.postDescription = postDescription;
        this.images = images;
    }
}
