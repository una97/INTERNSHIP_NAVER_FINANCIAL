package com.naverfinance.internproject.dto;

import com.naverfinance.internproject.model.entity.User;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class UserSaveDto {

    private Long id;


    @NotBlank
    @Size(min = 2, max = 12)
    private String userName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 15)
    private String password;

    @Builder
    public UserSaveDto(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public UserSaveDto(User user) { //조회를 위한 dto
        this.id = user.getId();
        this.userName = user.getUserName();
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public User toEntity() {
        return User.builder()
                .userName(userName)
                .email(email)
                .password(password)
                .build();
    }

}
