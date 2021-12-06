package com.naverfinance.internproject.service;

import com.naverfinance.internproject.dto.UserDeleteDto;
import com.naverfinance.internproject.dto.UserSaveDto;
import com.naverfinance.internproject.dto.UserUpdateDto;
import com.naverfinance.internproject.exception.PasswordNotMatchedException;
import com.naverfinance.internproject.exception.UserNotFoundException;
import com.naverfinance.internproject.model.entity.User;
import com.naverfinance.internproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User does not exist. id = " + id));
    }

    public List<UserSaveDto> getAllUsers() {
        return userRepository.findAll().stream().map(UserSaveDto::new).collect(Collectors.toList());
    }

    @Transactional// 중복 유저 가입 시
    public long createUser(UserSaveDto userSaveDto) {
        userSaveDto.setPassword(passwordEncoder.encode(userSaveDto.getPassword()));
        return userRepository.save(userSaveDto.toEntity()).getId();
    }


    public long updateUserPwd(long id, UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User does not exist. id = " + id));
        String prevPassword = userUpdateDto.getPrevPassword();
        if (!passwordEncoder.matches(prevPassword, user.getPassword())) {
            throw new PasswordNotMatchedException("Invalid password. Please try again.");
        }
        String encodedPassword = passwordEncoder.encode(userUpdateDto.getPassword());
        user.updatePassword(encodedPassword);
        return id;
    }


    public void deleteUser(long id, UserDeleteDto userDeleteDto) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User does not exist. id = " + id));
        if (!passwordEncoder.matches(userDeleteDto.getPassword(), user.getPassword())) {
            throw new PasswordNotMatchedException("Invalid password. Please try again.");
        }
        userRepository.delete(user);
    }

}
