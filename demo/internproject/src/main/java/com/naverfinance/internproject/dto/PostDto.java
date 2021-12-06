package com.naverfinance.internproject.dto;

import com.naverfinance.internproject.model.entity.Post;
import com.naverfinance.internproject.model.entity.User;
import lombok.*;

import javax.validation.constraints.*;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) //파라미터가 없는 기본 생성자
public class PostDto {

    private Long id;

    private User user;

    @Size(max = 20)
    @NotBlank
    private String title;

    @Size(max = 2000)
    @NotBlank
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder//필수적인 인자들을 포함하는 생성자를 만들기 위함
    public PostDto(Long id, User user, String title, String content,
                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }


    //DTO에서 필요한 부분을 빌더 패턴을 통해 Entity로 만드는 역할
    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .user(user)
                .build();
    }
}
