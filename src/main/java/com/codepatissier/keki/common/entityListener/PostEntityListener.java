package com.codepatissier.keki.common.entityListener;

import com.codepatissier.keki.common.BeanUtils;
import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.post.repository.PostLikeRepository;

import javax.persistence.PreRemove;

public class PostEntityListener {

    @PreRemove
    public void onUpdate(Post post){
        PostLikeRepository postLikeRepository = BeanUtils.getBean(PostLikeRepository.class);
        postLikeRepository.deleteByPost(post);
    }
}
