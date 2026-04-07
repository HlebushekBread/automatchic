package net.softloaf.automatchic.app.service.token;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Service
public class PasswordResetTokenService {
    private final StringRedisTemplate redisTemplate;

    private static final String TOKEN_PREFIX = "reset_token:";
    private static final String COOLDOWN_PREFIX = "reset_cooldown:";
    private static final long TOKEN_TTL = 5;
    private static final long COOLDOWN_TTL = 1;

    public String generateToken(String email) {
        String cooldownKey = COOLDOWN_PREFIX + email;
        Long remainingTtl = redisTemplate.getExpire(cooldownKey, TimeUnit.SECONDS);

        if (remainingTtl != null && remainingTtl > 0) {
            long minutes = remainingTtl / 60;
            long seconds = remainingTtl % 60;
            String timeLeft = String.format("%02d:%02d", minutes, seconds);

            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Повторная отправка возможна через: " + timeLeft);
        }

        String token = UUID.randomUUID().toString();
        String key = TOKEN_PREFIX + token;

        redisTemplate.opsForValue().set(key, email, Duration.ofMinutes(TOKEN_TTL));
        redisTemplate.opsForValue().set(cooldownKey, "cooldown", Duration.ofMinutes(COOLDOWN_TTL));

        return token;
    }

    public Optional<String> getEmailByToken(String token) {
        String key = TOKEN_PREFIX + token;
        String email = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(email);
    }

    public void deleteToken(String token) {
        redisTemplate.delete(TOKEN_PREFIX + token);
    }
}
