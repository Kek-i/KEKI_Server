package com.example.keki.post.service;

import com.example.keki.post.repository.PostRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }
}
