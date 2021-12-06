package com.naverfinance.internproject.service;


import com.naverfinance.internproject.InternprojectApplicationTests;

import com.naverfinance.internproject.model.entity.User;
import com.naverfinance.internproject.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest extends InternprojectApplicationTests {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    @Transactional
    @DisplayName("User 생성 테스트")
    public void createUser() {
        //given
        String userName = "hodal";
        String email = "elanise@naver.com";
        String pwd = "1234";

        String encodedPwd = passwordEncoder.encode(pwd);

        //when
        //TODO: 중복되는 userName이 있을 경우.
        userRepository.save(User.builder()
                .userName(userName)
                .email(email)
                .password(encodedPwd).build());

        // then
        Optional<User> user = userRepository.findByUserName(userName);
        assertTrue(user.isPresent());
        user.ifPresent(selectedUser -> {
            assertAll(
                    () -> assertEquals(selectedUser.getUserName(), userName),
                    () -> assertEquals(selectedUser.getEmail(), email),
                    () -> assertTrue(passwordEncoder.matches(pwd, encodedPwd))
            );

        });
    }

    @Test
    @DisplayName("존재하지 않는 사용자 조회")
    public void getNotExistedUser() {
        Optional<User> user = userRepository.findById(1L);
        user.ifPresent(selectedUser -> {
            System.out.println(selectedUser.getUserName());
        });
        assertFalse(user.isPresent());
    }

    @Test
    @DisplayName("존재하는 사용자 조회")
    public void getExistedUser() {
        Optional<User> user = userRepository.findById(10L);

        user.ifPresent(selectedUser -> {
            System.out.println(selectedUser.getUserName());
        });
        assertTrue(user.isPresent());
    }

    @Test
    @Transactional
    @DisplayName("User 비밀번호 업데이트 테스트")
    public void updateUserPwd() {
        //given
        String userName = "hodal";
        String email = "elanise@naver.com";
        String pwd = "1234";
        String encodedPwd = passwordEncoder.encode(pwd);

        String newPwd = "awefedsawef";
        String newEncodedPwd = passwordEncoder.encode(newPwd);

        //when
        userRepository.save(User.builder()
                .userName(userName)
                .email(email)
                .password(encodedPwd).build());

        //then
        Optional<User> user = userRepository.findByUserName(userName);
        assertTrue(user.isPresent());
        user.ifPresent(selectedUser -> {
            assertTrue(passwordEncoder.matches(pwd, selectedUser.getPassword())); //기존 비밀번호와 일치하는 지 확인
            selectedUser.updatePassword(newEncodedPwd);
            userRepository.save(selectedUser);
            assertTrue(passwordEncoder.matches(newPwd, selectedUser.getPassword()));
        });
    }

    @Test
    @Transactional
    @DisplayName("User 삭제 테스트")
    public void deleteUser() {
        Long fakeUserId = 10L;
        Optional<User> user = userRepository.findById(fakeUserId);

        assertTrue(user.isPresent());
        user.ifPresent(selectedUser -> {
            userRepository.delete(selectedUser);
        });
        Optional<User> deleteUser = userRepository.findById(fakeUserId);

        assertFalse(deleteUser.isPresent());

    }

}
