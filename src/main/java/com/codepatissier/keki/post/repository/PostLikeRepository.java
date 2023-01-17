package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.post.entity.PostLike;
import com.codepatissier.keki.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    boolean existsByPostAndUser(Post post, User user);
}
