package com.naverfinance.internproject.repository;

import com.naverfinance.internproject.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { //Entity Class와 PK의 자료형
    public Optional<User> findByUserName(String name);
}
