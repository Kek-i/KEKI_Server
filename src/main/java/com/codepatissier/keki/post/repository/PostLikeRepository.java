package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.post.entity.PostLike;
import com.codepatissier.keki.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByPostAndUser(Post post, User user);
    boolean existsByPostAndUserAndStatus(Post post, User user, String status);
}
