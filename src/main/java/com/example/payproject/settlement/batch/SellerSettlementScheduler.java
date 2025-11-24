package com.example.payproject.settlement.batch;

import com.example.payproject.seller.domain.SellerRepository;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;

@Component
@RequiredArgsConstructor
public class SellerSettlementScheduler {
    private static final Logger log = LoggerFactory.getLogger(SellerSettlementScheduler.class);
    private final SellerRepository sellerRepository;
    private final JobLauncher jobLauncher;
    private final Job sellerSettlementJob;
    @Value("${settlement.async.enabled}")
    private boolean settlementAsyncEnabled;
    private final ThreadPoolTaskExecutor settlementTaskExecutor;


    @Scheduled(cron = "${spring.task.scheduling.cron.settlement}")
    public void runMidnightSettlement(){
        Pageable pageable = Pageable.ofSize(100);
        Page<UUID> page;
        do{
            page = sellerRepository.findAll(pageable).map(seller -> seller.getId());
            List<UUID> sellerIds = page.getContent();
            if(sellerIds.isEmpty()){
                break;
            }
            log.info("Settlement batch chunk for {} sellers (page {}/{})",
                    sellerIds.size(), page.getNumber() +1, page.getTotalElements());
            sellerIds.forEach(this::runJobForSeller);
            pageable = page.hasNext() ? page.nextPageable() : Pageable.unpaged();
        }while(page.hasNext());
    }

    /**
     * SellerId에 대해 정산 Job을 실행한다
     * 비동기 설정 시 별도의 스레드에서 실행하고, 아니면 동기 실행한다.
     */
    private void runJobForSeller(UUID sellerId){
        try{
            Runnable executeJob = () -> {
                try{
                    JobParameters params = new JobParametersBuilder()
                            .addLong("timestamp", System.currentTimeMillis())
                            .addString("sellerId", sellerId.toString())
                            .toJobParameters();
                    jobLauncher.run(sellerSettlementJob, params);
                    log.info("Settlement job triggered for seller {}", sellerId);
                }catch(Exception ex){
                    log.error("Failed to run settlement job for seller {}", sellerId, ex);
                }
            };

            if (settlementAsyncEnabled) {
                settlementTaskExecutor.execute(executeJob);
            } else {
                executeJob.run();
            }
        } catch (Exception ex) {
            log.error("Failed to run settlement job for seller {}", sellerId, ex);
        }
    }

}
