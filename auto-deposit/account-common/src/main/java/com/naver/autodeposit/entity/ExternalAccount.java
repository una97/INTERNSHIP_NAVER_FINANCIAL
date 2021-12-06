package com.naver.autodeposit.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class ExternalAccount extends BaseTime {
    @Id
    private String accountNo;

    private long balance;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "naver_id", nullable = false, referencedColumnName = "naver_id")
    private NaverUser user;

    @Builder
    public ExternalAccount(String accountNo, long balance, NaverUser user) {
        this.accountNo = accountNo;
        this.balance = balance;
        this.user = user;
    }

    public void withdrawal(long depositAmount) {
        this.balance -= depositAmount;
    }
}
