package com.naverfinance.internproject.service;

import com.naverfinance.internproject.dto.PostDto;
import com.naverfinance.internproject.model.entity.Post;
import com.naverfinance.internproject.model.entity.User;
import com.naverfinance.internproject.repository.PostRepository;
import com.naverfinance.internproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post getPostById(long id) {
        return postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post does not exist. postId = " + id));
    }

    public String getAuthorName(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Post does not exist. postId = " + id));
        return post.getUser().getUserName();
    }

    public List<PostDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        List<PostDto> postDtoList = new ArrayList<>();

        for (Post post : posts) {
            PostDto postDto = PostDto.builder()
                    .id(post.getId())
                    .user(post.getUser())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .createdAt(post.getCreatedAt())
                    .updatedAt(post.getUpdatedAt())
                    .build();
            postDtoList.add(postDto);
        }

        return postDtoList;
    }


    @Transactional
    public long createPost(PostDto postDto) {
        User user = userRepository.findById(10L).orElseThrow(() -> new IllegalArgumentException("Insert Failed. postId = " + 10L)); //세션이 없어서 mock으로 대체
        postDto.setUser(user);
        return postRepository.save(postDto.toEntity()).getId();
    }


    @Transactional
    public long updatePost(long id, PostDto postDto) { //TODO: saveDto로
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Update Failed. postId = " + id));
        post.update(postDto.getTitle(), postDto.getContent());
        return id;
    }


    public void deletePost(long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Delete Failed. postId = " + id));
        postRepository.delete(post);
    }


}
