package com.codepatissier.keki.post.repository;

import com.codepatissier.keki.common.Tag.Tag;
import com.codepatissier.keki.post.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostTagCustom {
    List<Post> getPopularSortTagPosts(Tag searchTag, Pageable page);
    List<Post> getPopularSortTagPostsWithCursor(Tag searchTag, Long cursorIdx, Pageable page);
    List<Post> getLowPriceSortTagPosts(Tag searchTag, Pageable page);
    List<Post> getLowPriceSortTagPostsWithCursor(Tag searchTag, Long cursorIdx, Pageable page);
}
