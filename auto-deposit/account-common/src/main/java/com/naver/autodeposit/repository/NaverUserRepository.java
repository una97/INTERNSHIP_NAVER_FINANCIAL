package com.naver.autodeposit.repository;

import com.naver.autodeposit.entity.NaverUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaverUserRepository extends JpaRepository<NaverUser, Long> {
    NaverUser findByNaverId(String naverId);
}
