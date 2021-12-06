package com.naver.autodeposit.repository;

import com.naver.autodeposit.entity.NaverAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NaverAccountRepository extends JpaRepository<NaverAccount, String> {
    Optional<NaverAccount> findByAccountNo(String accountNo);
}
