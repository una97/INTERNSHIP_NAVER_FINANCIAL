package com.naver.autodeposit.service;

import com.naver.autodeposit.dto.autodeposit.balance.DepositBalanceCreateDto;
import com.naver.autodeposit.dto.autodeposit.balance.DepositBalanceUpdateDto;
import com.naver.autodeposit.dto.response.DepositOutputDto;
import com.naver.autodeposit.entity.DepositByBalance;
import com.naver.autodeposit.entity.NaverAccount;
import com.naver.autodeposit.entity.NaverUser;
import com.naver.autodeposit.enums.DepositState;
import com.naver.autodeposit.exception.AlreadyInvalidDepositException;
import com.naver.autodeposit.exception.DepositAlreadyExistsException;
import com.naver.autodeposit.exception.DepositNotFoundException;
import com.naver.autodeposit.repository.DepositByBalanceRepository;
import com.naver.autodeposit.response.ApiResponse;
import com.naver.autodeposit.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositByBalanceService {
    private final DepositByBalanceRepository depositByBalanceRepository;
    private final CommonAutoDepositService commonAutoDepositService;
    private final String LOGPREFIX = "[API][Balance]";

    public ApiResponse createAutoDepositBalance(DepositBalanceCreateDto depositBalanceDto) {
        commonAutoDepositService.checkAuthentication(depositBalanceDto.getNaverId(), depositBalanceDto.getPassword());

        String naverAccountNo = depositBalanceDto.getNaverAccountNo();
        NaverAccount naverAccount = commonAutoDepositService.getNaverAccount(naverAccountNo);
        commonAutoDepositService.checkLimitOfDeposit(naverAccount);

        depositBalanceDto.setNaverAccount(naverAccount);
        DepositByBalance depositByBalance = depositBalanceDto.toEntity();

        try {
            depositByBalanceRepository.save(depositByBalance);
        } catch (DataIntegrityViolationException e) {
            throw new DepositAlreadyExistsException("중복되는 예약 입금이 존재합니다");
        }

        log.info("{} User:'{}' created deposit:'{}'. {}", LOGPREFIX, naverAccount.getUser().getNaverId(), depositByBalance.getId(), depositByBalance.toString());
        return SuccessResponse.response(depositByBalance, "예약입금이 생성되었습니다.");
    }

    public ApiResponse updateDepositInvalid(long id) {
        DepositByBalance depositByBalance = getDepositByBalance(id);
        if (depositByBalance.getState().equals(DepositState.INVALID)) {
            throw new AlreadyInvalidDepositException("중지 중인 예약입금 입니다.");
        }
        depositByBalance.updateStateInvalid();
        depositByBalanceRepository.save(depositByBalance);

        DepositOutputDto depositOutputDto = DepositOutputDto.depositByBalance(depositByBalance);
        return SuccessResponse.response(depositOutputDto, "예약입금이 중지되었습니다.");
    }

    public ApiResponse updateBasisAndDepositAmount(DepositBalanceUpdateDto depositBalanceUpdateDto) {
        commonAutoDepositService.checkAuthentication(depositBalanceUpdateDto.getNaverId(), depositBalanceUpdateDto.getPassword());

        DepositByBalance depositByBalance = getDepositByBalance(depositBalanceUpdateDto.getId());
        long basisAmount = depositBalanceUpdateDto.getBasisAmount();
        long depositAmount = depositBalanceUpdateDto.getDepositAmount();

        depositByBalance.updateDepositDetail(basisAmount, depositAmount);
        depositByBalanceRepository.save(depositByBalance);

        log.info("{} User:'{}' updated deposit:'{}'. {}", LOGPREFIX, depositBalanceUpdateDto.getNaverId(), depositBalanceUpdateDto.getId(), depositBalanceUpdateDto.toString());
        return SuccessResponse.response(depositByBalance, "예약입금이 수정되었습니다.");
    }

    public ApiResponse deleteDeposit(long id) {
        DepositByBalance depositByBalance = getDepositByBalance(id);
        depositByBalanceRepository.delete(depositByBalance);
        NaverUser user = depositByBalance.getNaverAccount().getUser();

        log.info("{} User:'{}' deleted deposit:'{}'", LOGPREFIX, user.getNaverId(), id);
        return SuccessResponse.response(depositByBalance, "예약입금이 삭제되었습니다.");
    }

    public DepositByBalance getDepositByBalance(long id) {
        return depositByBalanceRepository.findById(id).orElseThrow(() -> new DepositNotFoundException("해당 예약입금이 존재하지 않습니다"));
    }
}
