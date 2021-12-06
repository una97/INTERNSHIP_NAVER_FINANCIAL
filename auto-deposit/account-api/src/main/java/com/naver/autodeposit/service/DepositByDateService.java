package com.naver.autodeposit.service;

import com.naver.autodeposit.dto.autodeposit.date.DepositDateCreateDto;
import com.naver.autodeposit.dto.autodeposit.date.DepositDateUpdateDto;
import com.naver.autodeposit.dto.response.DepositOutputDto;
import com.naver.autodeposit.entity.DepositByDate;
import com.naver.autodeposit.entity.NaverAccount;
import com.naver.autodeposit.entity.NaverUser;
import com.naver.autodeposit.enums.DepositState;
import com.naver.autodeposit.exception.AlreadyInvalidDepositException;
import com.naver.autodeposit.exception.DepositAlreadyExistsException;
import com.naver.autodeposit.exception.DepositNotFoundException;
import com.naver.autodeposit.repository.DepositByDateRepository;
import com.naver.autodeposit.response.ApiResponse;
import com.naver.autodeposit.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DepositByDateService {
    private final DepositByDateRepository depositByDateRepository;
    private final CommonAutoDepositService commonAutoDepositService;
    private final String LOGPREFIX = "[API][Date]";

    public ApiResponse createAutoDepositBalance(DepositDateCreateDto depositByDateDto) {
        commonAutoDepositService.checkAuthentication(depositByDateDto.getNaverId(), depositByDateDto.getPassword());

        String naverAccountNo = depositByDateDto.getNaverAccountNo();
        NaverAccount naverAccount = commonAutoDepositService.getNaverAccount(naverAccountNo);
        commonAutoDepositService.checkLimitOfDeposit(naverAccount);

        depositByDateDto.setNaverAccount(naverAccount);
        DepositByDate depositByDate = depositByDateDto.toEntity();

        try {
            depositByDateRepository.save(depositByDate);
        } catch (DataIntegrityViolationException e) {
            throw new DepositAlreadyExistsException("중복되는 예약 입금이 존재합니다");
        }

        log.info("{} User:'{}' created deposit:'{}'. {}", LOGPREFIX, naverAccount.getUser().getNaverId(), depositByDate.getId(), depositByDate.toString());
        return SuccessResponse.response(depositByDate, "예약입금이 생성되었습니다.");
    }

    public ApiResponse updateDepositInvalid(long id) {
        DepositByDate depositByDate = getDepositByDate(id);
        if (depositByDate.getState().equals(DepositState.INVALID)) {
            throw new AlreadyInvalidDepositException("중지 중인 예약입금 입니다.");
        }
        depositByDate.updateStateInvalid();
        depositByDateRepository.save(depositByDate);

        DepositOutputDto depositOutputDto = DepositOutputDto.depositByDate(depositByDate);
        return SuccessResponse.response(depositOutputDto, "예약입금이 중지되었습니다.");
    }

    public ApiResponse updateDayAndDepositAmount(DepositDateUpdateDto depositDateUpdateDto) {
        commonAutoDepositService.checkAuthentication(depositDateUpdateDto.getNaverId(), depositDateUpdateDto.getPassword());

        DepositByDate depositByDate = getDepositByDate(depositDateUpdateDto.getId());
        int dayOfMonth = depositDateUpdateDto.getDayOfMonth();
        long depositAmount = depositDateUpdateDto.getDepositAmount();

        depositByDate.updateDepositDetail(dayOfMonth, depositAmount);
        depositByDateRepository.save(depositByDate);

        log.info("{} User:'{}' updated deposit:'{}'. {}", LOGPREFIX, depositDateUpdateDto.getNaverId(), depositDateUpdateDto.getId(), depositDateUpdateDto.toString());
        return SuccessResponse.response(depositByDate, "예약입금이 수정되었습니다.");
    }

    public ApiResponse deleteDeposit(long id) {
        DepositByDate depositByDate = getDepositByDate(id);
        depositByDateRepository.delete(depositByDate);
        NaverUser user = depositByDate.getNaverAccount().getUser();

        log.info("{} User:'{}' deleted deposit:'{}'", LOGPREFIX, user.getNaverId(), id);
        return SuccessResponse.response(depositByDate, "예약입금이 삭제되었습니다.");
    }

    public DepositByDate getDepositByDate(long id) {
        return depositByDateRepository.findById(id).orElseThrow(() -> new DepositNotFoundException("해당 예약입금이 존재하지 않습니다"));
    }
}
