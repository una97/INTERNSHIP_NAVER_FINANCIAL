package com.naver.autodeposit.batchjob.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.OnReadError;
import org.springframework.batch.core.annotation.OnWriteError;

import java.util.List;

@Slf4j
public class DepositListener {
    public static class JobListener {
        @AfterJob
        public void afterJob(JobExecution jobExecution) {
            String JOB_NAME = jobExecution.getJobInstance().getJobName();
            int sum = jobExecution.getStepExecutions().stream().mapToInt(StepExecution::getWriteCount).sum();
            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                log.info("[{}][COMPLETED] TotalWrite: {}", JOB_NAME, sum);
            }
        }
    }

    public static class StepListener {
        @OnReadError
        public void onReadError(Exception e) {
            if (e instanceof RuntimeException) {
                log.error("An error occured while reading. Exception: {}", e);
            }
        }

        @OnWriteError
        public void onWriteError(Exception e, List items) {
            if (e instanceof RuntimeException) {
                log.error("An error occured while writing. Exception: {}", e);
            }
        }
    }
}
