package com.naver.autodeposit.repository;

import com.naver.autodeposit.entity.ExternalAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalAccountRepository extends JpaRepository<ExternalAccount, String> {
}
