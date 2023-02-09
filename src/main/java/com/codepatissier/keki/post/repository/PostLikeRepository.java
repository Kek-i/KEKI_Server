package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.post.entity.Post;
import com.codepatissier.keki.post.entity.PostLike;
import com.codepatissier.keki.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    PostLike findByPostAndUser(Post post, User user);
    List<PostLike> findByUserAndStatusOrderByLastModifiedDateDesc(User user, String status, Pageable page);
    List<PostLike> findByUserAndStatusAndLastModifiedDateLessThanOrderByLastModifiedDateDesc(User user, String status, LocalDateTime lastModifiedDate, Pageable page);
    boolean existsByPostAndUserAndStatus(Post post, User user, String status);
    boolean existsByUserAndStatusAndLastModifiedDateLessThan(User user, String status, LocalDateTime lastDate);

    void deleteByPost(Post post);
}
