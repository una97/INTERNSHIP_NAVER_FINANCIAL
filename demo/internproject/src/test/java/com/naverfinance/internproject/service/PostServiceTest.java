package com.naverfinance.internproject.service;

import com.naverfinance.internproject.InternprojectApplicationTests;
import com.naverfinance.internproject.model.entity.Post;
import com.naverfinance.internproject.model.entity.User;
import com.naverfinance.internproject.repository.PostRepository;
import com.naverfinance.internproject.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


public class PostServiceTest extends InternprojectApplicationTests {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("Post 생성")
    public void create() { //FIXME: repository에 의존도가 많은 테스트 코드
        //given
        String title = "TITLE NEW TITLE";
        String content = "Demo project using Spring Boot";
        Long fakeUserId = 10L;
        Optional<User> user = userRepository.findById(fakeUserId);

        assertThat(user.isPresent());
        //when
        user.ifPresent(selectUser -> {
            postRepository.save(Post.builder()
                    .title(title)
                    .content(content)
                    .user(selectUser)
                    .build());
        });

        List<Post> posts = postRepository.findAll();
        int len = posts.size();
        Post post = posts.get(len - 1);

        //then
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getUser().getId()).isEqualTo(fakeUserId);
    }

    @Test
    @DisplayName("CreatedAt 자동 저장")
    public void saveBaseTime() {
        //given
        Optional<User> user = userRepository.findById(10L);
        assertThat(user.isPresent());
        LocalDateTime now = LocalDateTime.now();

        //when
        user.ifPresent(selectUser -> {
            postRepository.save(Post.builder()
                    .title("title")
                    .content("content")
                    .user(selectUser)
                    .build());
        });

        List<Post> posts = postRepository.findAll();
        int len = posts.size();
        Post post = posts.get(len - 1);

        //then
        assertThat(post.getCreatedAt()).isAfter(now);
        assertThat(post.getUpdatedAt()).isAfter(now);
    }


}
