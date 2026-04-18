package net.softloaf.automatchic.app.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import net.softloaf.automatchic.app.repository.LinkRepository;
import net.softloaf.automatchic.app.repository.SubjectRepository;
import net.softloaf.automatchic.app.repository.TaskRepository;
import net.softloaf.automatchic.app.repository.UserRepository;
import net.softloaf.automatchic.common.metrics.Metrics;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Gauge unconfirmedUsersGauge(MeterRegistry registry, UserRepository userRepository) {
        return Gauge.builder(Metrics.USERS_UNCONFIRMED_CURRENT, userRepository,
                        repo -> repo.countByIsConfirmed(false))
                .description("Current number of unconfirmed users in DB")
                .register(registry);
    }

    @Bean
    public Gauge confirmedUsersGauge(MeterRegistry registry, UserRepository userRepository) {
        return Gauge.builder(Metrics.USERS_CONFIRMED_CURRENT, userRepository,
                        repo -> repo.countByIsConfirmed(true))
                .description("Current number of confirmed users in DB")
                .register(registry);
    }

    @Bean
    public Gauge totalSubjectsGauge(MeterRegistry registry, SubjectRepository subjectRepository) {
        return Gauge.builder(Metrics.SUBJECTS_CURRENT, subjectRepository,
                        repo -> repo.count())
                .description("Current number of subjects in DB")
                .register(registry);
    }

    @Bean
    public Gauge totalTasksGauge(MeterRegistry registry, TaskRepository taskRepository) {
        return Gauge.builder(Metrics.TASKS_CURRENT, taskRepository,
                        repo -> repo.count())
                .description("Current number tasks in DB")
                .register(registry);
    }

    @Bean
    public Gauge totalLinksGauge(MeterRegistry registry, LinkRepository linkRepository) {
        return Gauge.builder(Metrics.LINKS_CURRENT, linkRepository,
                        repo -> repo.count())
                .description("Current number tasks in DB")
                .register(registry);
    }
}
