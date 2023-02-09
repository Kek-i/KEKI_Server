package com.codepatissier.keki.post.entity;

import com.codepatissier.keki.common.BaseEntity;
import com.codepatissier.keki.common.entityListener.PostEntityListener;
import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.store.entity.Store;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@DynamicInsert
@SQLDelete(sql = "UPDATE post SET status = 'inactive' WHERE post_idx = ?")
@EntityListeners(PostEntityListener.class)
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "post", orphanRemoval = true)
    private List<PostTag> tags = new ArrayList<>();

    @Builder
    public Post(Dessert dessert, Store store, String postDescription) {
        this.dessert = dessert;
        this.store = store;
        this.postDescription = postDescription;
    }

    public void setDessert(Dessert dessert) {
        this.dessert = dessert;
    }
    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }
}
