package com.naver.autodeposit.controller;

import com.naver.autodeposit.dto.response.DepositOutputDto;
import com.naver.autodeposit.enums.DepositState;
import com.naver.autodeposit.enums.DepositType;
import com.naver.autodeposit.response.SuccessResponse;
import com.naver.autodeposit.service.DepositByBalanceService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(DepositByBalanceController.class)
class DepositByBalanceControllerTest {
    @Autowired
    private MockMvc mvc;
    
    @MockBean
    private DepositByBalanceService depositByBalanceService;

    @Test
    void deleteDeposit() throws Exception {
        DepositOutputDto depositOutputDto = DepositOutputDto.builder()
                .id(1).basisAmount(20000).depositAmount(30000).type(DepositType.BALANCE)
                .state(DepositState.VALID).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        Mockito.when(depositByBalanceService.deleteDeposit(1)).thenReturn(SuccessResponse.response(depositOutputDto, "[SUCCESS] Delete deposit"));

        mvc.perform(delete("/auto-deposit/balance/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("[SUCCESS] Delete deposit"))
                .andExpect(jsonPath("$.body.id").value(1))
                .andExpect(jsonPath("$.body.basisAmount").value(20000))
                .andExpect(jsonPath("$.body.depositAmount").value(30000))
                .andExpect(jsonPath("$.body.state").value(String.valueOf(DepositState.VALID)))
                .andDo(print());
    }
}