package net.softloaf.automatchic.app.service.util;


import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.softloaf.automatchic.app.repository.UserRepository;
import net.softloaf.automatchic.common.metrics.Metrics;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserCleanupService {
    private final UserRepository userRepository;
    private final MeterRegistry meterRegistry;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void removeUnverifiedUsers() {
        log.info("Запуск очистки базы...");
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);

        int deletedCount = userRepository.deleteByIsConfirmedFalseAndRegisteredAtBefore(cutoff);

        if (deletedCount > 0) {
            meterRegistry.counter(Metrics.USERS_CLEANED_TOTAL).increment(deletedCount);
            log.info("Удалено неподтвержденных аккаунтов: {}", deletedCount);
        }
    }
}