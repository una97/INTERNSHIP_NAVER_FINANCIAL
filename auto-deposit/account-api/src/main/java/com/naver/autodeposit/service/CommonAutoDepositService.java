package com.naver.autodeposit.service;

import com.naver.autodeposit.dto.account.AccountDepositDto;
import com.naver.autodeposit.dto.account.AccountWithdrawalDto;
import com.naver.autodeposit.dto.response.DepositOutputDto;
import com.naver.autodeposit.entity.DepositByBalance;
import com.naver.autodeposit.entity.DepositByDate;
import com.naver.autodeposit.entity.NaverAccount;
import com.naver.autodeposit.entity.NaverUser;
import com.naver.autodeposit.exception.*;
import com.naver.autodeposit.repository.DepositByBalanceRepository;
import com.naver.autodeposit.repository.DepositByDateRepository;
import com.naver.autodeposit.repository.NaverAccountRepository;
import com.naver.autodeposit.repository.NaverUserRepository;
import com.naver.autodeposit.response.ApiResponse;
import com.naver.autodeposit.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommonAutoDepositService {
    private final DepositByBalanceRepository depositByBalanceRepository;
    private final DepositByDateRepository depositByDateRepository;
    private final NaverAccountRepository naverAccountRepository;
    private final NaverUserRepository naverUserRepository;

    public static final long LIMIT_OF_DEPOSIT = 6;

    public void checkLimitOfDeposit(NaverAccount naverAccount) {
        long numOfAutoDeposits = depositByBalanceRepository.countByNaverAccount(naverAccount) + depositByDateRepository.countByNaverAccount(naverAccount);
        if (numOfAutoDeposits >= LIMIT_OF_DEPOSIT)
            throw new TooManyAutoDepositException("한 사람 당 예약 입금은 " + LIMIT_OF_DEPOSIT + "개 까지 생성 가능합니다");
    }

    public ApiResponse getAutoDeposits(String naverAccountNo) {
        NaverAccount naverAccount = naverAccountRepository.findByAccountNo(naverAccountNo).orElseThrow(() -> new NaverAccountNotFoundException("해당 네이버 통장이 존재하지 않습니다."));
        List<DepositOutputDto> depositOutput = new ArrayList<>();

        List<DepositByBalance> depositBalances = depositByBalanceRepository.findByNaverAccount(naverAccount);
        List<DepositByDate> depositByDates = depositByDateRepository.findByNaverAccount(naverAccount);

        for (DepositByBalance data : depositBalances) {
            DepositOutputDto deposit = DepositOutputDto.depositByBalance(data);
            depositOutput.add(deposit);
        }

        for (DepositByDate data : depositByDates) {
            DepositOutputDto deposit = DepositOutputDto.depositByDate(data);
            depositOutput.add(deposit);
        }

        return SuccessResponse.response(depositOutput, "예약 입금 수: " + depositOutput.size());
    }

    public ApiResponse deposit(AccountDepositDto accountDepositDto) {
        NaverAccount naverAccount = getNaverAccount(accountDepositDto.getNaverAccountNo());
        long depositAmount = accountDepositDto.getDepositAmount();
        try {
            naverAccount.deposit(depositAmount);
        } catch (IllegalArgumentException e) {
            throw new BalanceExceedException("네이버 통장의 보유 가능한 금액을 초과했습니다.");
        }
        naverAccountRepository.save(naverAccount);
        return SuccessResponse.response(naverAccount.getBalance(), String.format("성공적으로 %,d원 입금되었습니다.", depositAmount));
    }

    public ApiResponse withdrawal(AccountWithdrawalDto accountWithdrawalDto) {
        NaverAccount naverAccount = getNaverAccount(accountWithdrawalDto.getNaverAccountNo());
        long withdrawalAmount = accountWithdrawalDto.getWithdrawalAmount();
        try {
            naverAccount.withdrawal(withdrawalAmount);
        } catch (IllegalArgumentException e) {
            throw new WithdrawalFailException("잔액이 부족해 출금할 수 없습니다.");
        }
        naverAccountRepository.save(naverAccount);
        return SuccessResponse.response(naverAccount.getBalance(), String.format("성공적으로 %,d원 출금되었습니다.", withdrawalAmount));
    }

    public NaverAccount getNaverAccount(String naverAccountNo) {
        return naverAccountRepository.findByAccountNo(naverAccountNo).orElseThrow(() -> new NaverAccountNotFoundException("존재하지 않는 통장번호입니다."));
    }

    public void checkAuthentication(String naverId, String password) {
        NaverUser naverUser = naverUserRepository.findByNaverId(naverId);
        if (!naverUser.getPassword().equals(password)) {
            throw new PasswordNotMatchedException("비밀번호가 일치하지 않습니다. 다시 시도하세요.");
        }
    }
}
