package com.naver.autodeposit.batchjob;

import com.naver.autodeposit.batchjob.dto.DepositBatchDto;
import com.naver.autodeposit.batchjob.listener.DepositListener;
import com.naver.autodeposit.batchjob.service.DepositBatchService;
import com.naver.autodeposit.entity.AutoDepositHistory;
import com.naver.autodeposit.entity.DepositByBalance;
import com.naver.autodeposit.entity.ExternalAccount;
import com.naver.autodeposit.entity.NaverAccount;
import com.naver.autodeposit.enums.DepositState;
import com.naver.autodeposit.enums.DepositType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DepositByBalanceJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DepositItemWriter depositItemWriter;
    private final DepositBatchService depositBatchService;

    private static final String JOB_NAME = "depositBalanceJob";
    private static final int CHUNK = 20;
    private final String LOGPREFIX = "[BATCH][Balance]";

    @Bean(JOB_NAME)
    public Job depositJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(depositByBalanceStep())
                .listener(new DepositListener.JobListener())
                .build();
    }

    @Bean(JOB_NAME + "_saveStep")
    public Step depositByBalanceStep() {
        return stepBuilderFactory.get(JOB_NAME + "_saveStep")
                .<DepositByBalance, DepositBatchDto>chunk(CHUNK)
                .reader(depositReader())
                .processor(depositProcessor())
                .writer(depositItemWriter.saveBalanceAndHistory())
                .faultTolerant()
                .listener(new DepositListener.StepListener())
                .build();
    }

    @Bean(name = JOB_NAME + "_reader", destroyMethod = "")
    public JpaPagingItemReader<DepositByBalance> depositReader() {
        Map<String, Object> queryParameter = new HashMap<>();
        queryParameter.put("state", DepositState.VALID);

        return new JpaPagingItemReaderBuilder<DepositByBalance>()
                .queryString("select d from DepositByBalance as d where d.state = :state order by id")
                .parameterValues(queryParameter)
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK)
                .name(JOB_NAME + "_reader")
                .build();
    }

    public ItemProcessor<DepositByBalance, DepositBatchDto> depositProcessor() {
        return item -> {
            NaverAccount naverAccount = item.getNaverAccount();
            ExternalAccount externalAccount = naverAccount.getExternalAccount();

            if (externalAccount.getBalance() < item.getDepositAmount()) {
                log.info("{}[FILTERED] id='{}'. ExternalAccount='{}' has not enough money.", LOGPREFIX, item.getId(), externalAccount.getAccountNo());
                return null;
            }

            if (naverAccount.getBalance() > item.getBasisAmount()) {
                log.info("{}[FILTERED] id='{}'. BalanceAmount is greater than basisAmount.", LOGPREFIX, item.getId());
                return null;
            }

            if (depositBatchService.isOverBalanceMax(naverAccount, item.getDepositAmount())) {
                log.info("{}[FILTERED] id='{}'. NaverAccount='{}' has exceeded the maximum amount.", LOGPREFIX, item.getId(), externalAccount.getAccountNo());
                return null;
            }

            depositBatchService.executeDeposit(naverAccount, item.getDepositAmount());
            log.info("{}[SUCCESS] id='{}'. DepositAmount='{}' {}", LOGPREFIX, item.getId(), item.getDepositAmount(), naverAccount.toString());

            AutoDepositHistory autoDepositHistory = AutoDepositHistory.builder()
                    .naverAccountNo(naverAccount.getAccountNo())
                    .externalAccountNo(externalAccount.getAccountNo())
                    .type(DepositType.BALANCE)
                    .depositAmount(item.getDepositAmount())
                    .balance(naverAccount.getBalance())
                    .build();

            return new DepositBatchDto(naverAccount, autoDepositHistory);
        };
    }

}
