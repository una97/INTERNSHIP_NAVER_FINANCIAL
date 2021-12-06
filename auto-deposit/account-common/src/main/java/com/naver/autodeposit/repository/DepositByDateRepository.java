package com.naver.autodeposit.repository;

import com.naver.autodeposit.entity.DepositByDate;
import com.naver.autodeposit.entity.NaverAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositByDateRepository extends JpaRepository<DepositByDate, Long> {
    long countByNaverAccount(NaverAccount naverAccountNo);

    List<DepositByDate> findByNaverAccount(NaverAccount naverAccount);
}
