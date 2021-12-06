package com.naver.autodeposit.repository;

import com.naver.autodeposit.entity.AutoDepositHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AutoDepositHistoryRepository extends JpaRepository<AutoDepositHistory, Long> {
}
