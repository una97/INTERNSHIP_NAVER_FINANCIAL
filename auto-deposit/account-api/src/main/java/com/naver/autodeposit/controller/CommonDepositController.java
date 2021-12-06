package com.naver.autodeposit.controller;

import com.naver.autodeposit.dto.account.AccountDepositDto;
import com.naver.autodeposit.dto.account.AccountWithdrawalDto;
import com.naver.autodeposit.dto.autodeposit.DepositReadDto;
import com.naver.autodeposit.response.ApiResponse;
import com.naver.autodeposit.service.CommonAutoDepositService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/naver-account/")
public class CommonDepositController {
    private final CommonAutoDepositService commonAutoDepositService;

    @GetMapping("/")
    public ResponseEntity getAutoDeposits(@Valid @RequestBody DepositReadDto depositReadDto) {
        ApiResponse response = commonAutoDepositService.getAutoDeposits(depositReadDto.getNaverAccountNo());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/deposit")
    public ResponseEntity deposit(@Valid @RequestBody AccountDepositDto accountDepositDto) {
        ApiResponse response = commonAutoDepositService.deposit(accountDepositDto);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/withdrawal")
    public ResponseEntity withdraw(@Valid @RequestBody AccountWithdrawalDto accountWithdrawalDto) {
        ApiResponse response = commonAutoDepositService.withdrawal(accountWithdrawalDto);
        return ResponseEntity.ok().body(response);
    }


}
