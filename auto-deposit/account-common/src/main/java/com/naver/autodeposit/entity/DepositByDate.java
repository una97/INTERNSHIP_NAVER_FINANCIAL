package com.naver.autodeposit.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.naver.autodeposit.enums.DepositState;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Entity
public class DepositByDate extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "naver_account_no", nullable = false)
    @JsonIgnore
    private NaverAccount naverAccount;

    private int dayOfMonth;

    private long depositAmount;

    @Enumerated(EnumType.STRING)
    private DepositState state;

    @Builder
    public DepositByDate(NaverAccount naverAccount, int dayOfMonth, long depositAmount, DepositState state) {
        this.naverAccount = naverAccount;
        this.dayOfMonth = dayOfMonth;
        this.depositAmount = depositAmount;
        this.state = state;
    }

    public void updateStateInvalid() {
        this.state = DepositState.INVALID;
    }

    public void updateDepositDetail(int dayOfMonth, long depositAmount) {
        this.dayOfMonth = dayOfMonth;
        this.depositAmount = depositAmount;
    }

    @Override
    public String toString() {
        return "{dayOfMonth=" + dayOfMonth +
                ", depositAmount=" + depositAmount +
                "}";
    }
}
