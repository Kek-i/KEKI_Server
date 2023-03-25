package com.codepatissier.keki.common.entityListener;

import com.codepatissier.keki.common.BeanUtils;
import com.codepatissier.keki.dessert.entity.Dessert;
import com.codepatissier.keki.post.repository.PostRepository;

import javax.persistence.PreRemove;

public class DessertEntityListener {

    @PreRemove
    public void onUpdate(Dessert dessert){
        PostRepository postRepository = BeanUtils.getBean(PostRepository.class);
        postRepository.deleteByDessert(dessert);
    }
}
