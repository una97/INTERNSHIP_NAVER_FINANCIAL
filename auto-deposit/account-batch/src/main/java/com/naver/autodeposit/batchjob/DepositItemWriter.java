package com.naver.autodeposit.batchjob;

import com.naver.autodeposit.batchjob.dto.DepositBatchDto;
import com.naver.autodeposit.repository.AutoDepositHistoryRepository;
import com.naver.autodeposit.repository.NaverAccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DepositItemWriter {
    private final NaverAccountRepository naverAccountRepository;
    private final AutoDepositHistoryRepository autoDepositHistoryRepository;

    @Bean
    public ItemWriter<DepositBatchDto> saveBalanceAndHistory() {
        return ((List<? extends DepositBatchDto> items) -> {
            for (DepositBatchDto item : items) {
                naverAccountRepository.save(item.getNaverAccount());
                autoDepositHistoryRepository.save(item.getAutoDepositHistory());
            }
        });
    }
}
