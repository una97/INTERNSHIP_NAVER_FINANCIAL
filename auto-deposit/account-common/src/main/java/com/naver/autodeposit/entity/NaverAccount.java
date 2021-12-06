package com.naver.autodeposit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naver.autodeposit.GlobalConstant;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class NaverAccount extends BaseTime {
    @Id
    private String accountNo;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "external_account_no", nullable = false)
    @JsonIgnore
    private ExternalAccount externalAccount;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "naver_id", referencedColumnName = "naver_id")
    private NaverUser user;

    private long balance;

    @Builder
    public NaverAccount(String accountNo, ExternalAccount externalAccount, long balance, NaverUser user) {
        this.accountNo = accountNo;
        this.externalAccount = externalAccount;
        this.balance = balance;
        this.user = user;
    }

    public void deposit(long depositAmount) {
        if (this.balance + depositAmount > GlobalConstant.BALANCE_MAX) {
            throw new IllegalArgumentException();
        }
        this.balance += depositAmount;
    }

    public void withdrawal(long withdrawalAmount) {
        if (this.balance < withdrawalAmount) {
            throw new IllegalArgumentException();
        }
        this.balance -= withdrawalAmount;
    }

    @Override
    public String toString() {
        return "{ExternalAccount= '" + externalAccount.getAccountNo() +
                "', NaverAccount= '" + accountNo +
                "', NaverAccountBalance ='" + balance + "'}";
    }
}
