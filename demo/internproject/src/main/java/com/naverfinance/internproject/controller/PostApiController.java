package com.naverfinance.internproject.controller;

import com.naverfinance.internproject.dto.PostDto;
import com.naverfinance.internproject.model.entity.Post;
import com.naverfinance.internproject.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostApiController {

    private final PostService postService;

    @GetMapping("/{id}")
    public Post getPost(@PathVariable long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/")
    public List<PostDto> getAllPosts() {
        List<PostDto> postDtos = postService.getAllPosts();
        return postDtos;
    }

    @GetMapping("/author/{id}")
    public String getAuthorName(@PathVariable long id) {
        return postService.getAuthorName(id);
    }

    @PostMapping("/")
    public long createPost(@Valid @RequestBody PostDto postDto) {
        return postService.createPost(postDto);
    }

    @PatchMapping("/{id}")
    public long updatePost(@PathVariable long id, @Valid @RequestBody PostDto postDto) { //TODO: request, save마다 Dto가 다름.
        postService.updatePost(id, postDto);
        return id;
    }

    @DeleteMapping("/{id}")
    public long deletePost(@PathVariable long id) {
        postService.deletePost(id);
        return id; // 다시 보기
    }
}
