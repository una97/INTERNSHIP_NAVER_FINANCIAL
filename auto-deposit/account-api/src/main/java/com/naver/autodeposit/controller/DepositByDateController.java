package com.naver.autodeposit.controller;

import com.naver.autodeposit.dto.autodeposit.date.DepositDateCreateDto;
import com.naver.autodeposit.dto.autodeposit.date.DepositDateUpdateDto;
import com.naver.autodeposit.response.ApiResponse;
import com.naver.autodeposit.service.DepositByDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auto-deposit/date")
public class DepositByDateController {
    private final DepositByDateService depositByDateService;

    @PostMapping("/")
    public ResponseEntity createAutoDepositDate(@Valid @RequestBody DepositDateCreateDto depositByDateDto) {
        ApiResponse response = depositByDateService.createAutoDepositBalance(depositByDateDto);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity updateDepositInvalid(@PathVariable long id) {
        ApiResponse response = depositByDateService.updateDepositInvalid(id);
        return ResponseEntity.ok().body(response);
    }

    @PatchMapping("/")
    public ResponseEntity updateDayAndDepositAmount(@Valid @RequestBody DepositDateUpdateDto depositDateUpdateDto) {
        ApiResponse response = depositByDateService.updateDayAndDepositAmount(depositDateUpdateDto);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteDeposit(@PathVariable long id) {
        ApiResponse response = depositByDateService.deleteDeposit(id);
        return ResponseEntity.ok().body(response);
    }

}
