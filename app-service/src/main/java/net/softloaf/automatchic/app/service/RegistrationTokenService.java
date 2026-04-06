package net.softloaf.automatchic.app.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RegistrationTokenService {
    private final StringRedisTemplate redisTemplate;

    private static final String TOKEN_PREFIX = "confirm_token:";
    private static final long TOKEN_TTL = 60;

    public String generateToken(String email) {
        String token = UUID.randomUUID().toString();
        String key = TOKEN_PREFIX + token;

        redisTemplate.opsForValue().set(key, email, Duration.ofMinutes(TOKEN_TTL));

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
