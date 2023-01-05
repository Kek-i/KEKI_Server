package com.example.keki.store.service;

import com.example.keki.store.repository.PostRepository;
import com.example.keki.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    private PostRepository postRepository;

    public PostService(PostRepository postRepository){
        this.postRepository = postRepository;
    }
}
