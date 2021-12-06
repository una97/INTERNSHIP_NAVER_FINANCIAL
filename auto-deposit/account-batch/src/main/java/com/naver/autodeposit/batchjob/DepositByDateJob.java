package com.naver.autodeposit.batchjob;

import com.naver.autodeposit.batchjob.dto.DepositBatchDto;
import com.naver.autodeposit.batchjob.listener.DepositListener;
import com.naver.autodeposit.batchjob.service.DepositBatchService;
import com.naver.autodeposit.entity.AutoDepositHistory;
import com.naver.autodeposit.entity.DepositByDate;
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
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DepositByDateJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EntityManagerFactory entityManagerFactory;
    private final DepositItemWriter depositItemWriter;
    private final DepositBatchService depositBatchService;

    private static final String JOB_NAME = "depositDateJob";
    private static final int CHUNK = 20;
    private static final Map<String, Object> queryParameter = new HashMap<>();
    private static final LocalDate currentDate = LocalDate.now();
    private static final LocalDate lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    private final String LOGPREFIX = "[BATCH][Date]";

    @Bean(JOB_NAME)
    public Job depositDateJob() {
        return jobBuilderFactory.get(JOB_NAME)
                .incrementer(new RunIdIncrementer())
                .start(depositByDateStep())
                .listener(new DepositListener.JobListener())
                .build();
    }

    @Bean(JOB_NAME + "_saveStep")
    public Step depositByDateStep() {
        return stepBuilderFactory.get(JOB_NAME + "_saveStep")
                .<DepositByDate, DepositBatchDto>chunk(CHUNK)
                .reader(depositDateReader())
                .processor(depositDateProcessor())
                .writer(depositItemWriter.saveBalanceAndHistory())
                .build();
    }

    @Bean(name = JOB_NAME + "_reader", destroyMethod = "")
    public JpaPagingItemReader<DepositByDate> depositDateReader() {
        queryParameter.put("state", DepositState.VALID);
        queryParameter.put("day", currentDate.getDayOfMonth());

        String query = "select d from DepositByDate as d where d.state = :state and d.dayOfMonth %s :day order by id";
        if (currentDate.isEqual(lastDayOfMonth)) {
            query = String.format(query, ">=");
        } else if (currentDate.isBefore(lastDayOfMonth)) {
            query = String.format(query, "=");
        }

        return new JpaPagingItemReaderBuilder<DepositByDate>()
                .queryString(query)
                .parameterValues(queryParameter)
                .entityManagerFactory(entityManagerFactory)
                .pageSize(CHUNK)
                .name("depositDateNormalReader")
                .build();
    }

    public ItemProcessor<DepositByDate, DepositBatchDto> depositDateProcessor() {
        return item -> {
            NaverAccount naverAccount = item.getNaverAccount();
            ExternalAccount externalAccount = naverAccount.getExternalAccount();

            if (externalAccount.getBalance() < item.getDepositAmount()) {
                log.info("{}[FILTERED] id='{}'. ExternalAccount='{}' has not enough money.", LOGPREFIX, item.getId(), externalAccount.getAccountNo());
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
                    .type(DepositType.DATE)
                    .depositAmount(item.getDepositAmount())
                    .balance(naverAccount.getBalance())
                    .build();

            return new DepositBatchDto(naverAccount, autoDepositHistory);
        };
    }

}

