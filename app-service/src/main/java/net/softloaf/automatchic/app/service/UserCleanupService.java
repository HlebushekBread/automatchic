package net.softloaf.automatchic.app.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.softloaf.automatchic.app.repository.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserCleanupService {

    private final UserRepository userRepository;

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void removeUnverifiedUsers() {
        log.info("Запуск очистки базы...");
        LocalDateTime cutoff = LocalDateTime.now().minusDays(1);

        int deletedCount = userRepository.deleteByIsEnabledFalseAndRegisteredAtBefore(cutoff);

        if (deletedCount > 0) {
            log.info("Удалено неподтвержденных аккаунтов: {}", deletedCount);
        }
    }
}