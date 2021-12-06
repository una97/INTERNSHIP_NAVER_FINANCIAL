package com.naver.autodeposit.repository;

import com.naver.autodeposit.entity.DepositByBalance;
import com.naver.autodeposit.entity.NaverAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositByBalanceRepository extends JpaRepository<DepositByBalance, Long> {
    long countByNaverAccount(NaverAccount naverAccountNo);

    List<DepositByBalance> findByNaverAccount(NaverAccount naverAccount);
}
