package com.naverfinance.internproject.controller;

import com.naverfinance.internproject.dto.UserDeleteDto;
import com.naverfinance.internproject.dto.UserSaveDto;
import com.naverfinance.internproject.dto.UserUpdateDto;
import com.naverfinance.internproject.model.entity.User;
import com.naverfinance.internproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserApiController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User getUser(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @GetMapping(path = "/")
    public List<UserSaveDto> getAllUsers() {
        List<UserSaveDto> userDtos = userService.getAllUsers();
        return userDtos;
    }

    @PostMapping("/")
    public long createUser(@Valid @RequestBody UserSaveDto userSaveDto) {
        return userService.createUser(userSaveDto);
    }

    @PatchMapping("/{id}")
    public long updateUserPwd(@PathVariable long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return userService.updateUserPwd(id, userUpdateDto);

    }

    @DeleteMapping("/{id}")
    public long deleteUser(@PathVariable long id, @Valid @RequestBody UserDeleteDto userDeleteDto) {
        userService.deleteUser(id, userDeleteDto);
        return id;
    }

}
