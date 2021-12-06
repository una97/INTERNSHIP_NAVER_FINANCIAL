package com.naver.autodeposit.controller;

import com.naver.autodeposit.dto.autodeposit.balance.DepositBalanceCreateDto;
import com.naver.autodeposit.dto.autodeposit.balance.DepositBalanceUpdateDto;
import com.naver.autodeposit.response.ApiResponse;
import com.naver.autodeposit.service.DepositByBalanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auto-deposit/balance")
public class DepositByBalanceController {
    private final DepositByBalanceService depositByBalanceService;

    @PostMapping("/")
    public ResponseEntity createAutoDepositBalance(@Valid @RequestBody DepositBalanceCreateDto depositByBalanceCreateDto) {
        ApiResponse response = depositByBalanceService.createAutoDepositBalance(depositByBalanceCreateDto);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateDepositInvalid(@PathVariable long id) {
        ApiResponse response = depositByBalanceService.updateDepositInvalid(id);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/")
    public ResponseEntity updateBasisAndDepositAmount(@Valid @RequestBody DepositBalanceUpdateDto depositBalanceUpdateDto) {
        ApiResponse response = depositByBalanceService.updateBasisAndDepositAmount(depositBalanceUpdateDto);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDeposit(@PathVariable long id) {
        ApiResponse response = depositByBalanceService.deleteDeposit(id);
        return ResponseEntity.ok().body(response);
    }
}
