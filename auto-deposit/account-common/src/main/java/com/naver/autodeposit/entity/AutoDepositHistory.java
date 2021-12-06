package com.naver.autodeposit.entity;

import com.naver.autodeposit.enums.DepositType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class AutoDepositHistory extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String naverAccountNo;

    private String externalAccountNo;

    @Enumerated(EnumType.STRING)
    private DepositType type;

    private long balance;

    private long depositAmount;

    @Builder
    public AutoDepositHistory(String naverAccountNo, String externalAccountNo, DepositType type, long balance, long depositAmount) {
        this.naverAccountNo = naverAccountNo;
        this.externalAccountNo = externalAccountNo;
        this.type = type;
        this.balance = balance;
        this.depositAmount = depositAmount;
    }
}
