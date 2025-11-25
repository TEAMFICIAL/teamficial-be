package teamficial.teamficial_be.global.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import teamficial.teamficial_be.global.apiPayload.code.status.ErrorStatus;
import teamficial.teamficial_be.global.apiPayload.exception.GeneralException;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String APPLICANT_COUNT_KEY_PREFIX = "recruit:post:";

    public void setValue(String key, String value, long ttlMillis) {
        try {
            if (ttlMillis > 0) {
                ValueOperations<String, Object> values = redisTemplate.opsForValue();
                values.set(key, value, Duration.ofMillis(ttlMillis));
            } else {
                ValueOperations<String, Object> values = redisTemplate.opsForValue();
                values.set(key, value);
            }

        } catch (Exception e) {
            log.error("Redis set 오류 — key: {}, ttl: {}, 예외: {}", key, ttlMillis, e.toString(), e);
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public String getValue(String key) {
        try {
            ValueOperations<String, Object> values = redisTemplate.opsForValue();
            if (values.get(key) == null) {
                return "";
            }
            return values.get(key).toString();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public void deleteValue(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public boolean checkExistsValue(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public void incrementValue(String key, long delta) {
        try {
            ValueOperations<String, Object> ops = redisTemplate.opsForValue();
            ops.increment(key, delta);
        } catch (Exception e) {
            log.warn("Redis 지원자 수 increment 오류 — key: {}, delta: {}, 예외: {}", key, delta, e.toString(), e);
            throw new GeneralException(ErrorStatus.REDIS_ERROR);
        }
    }

    public String applicationKey(Long postId) {
        return APPLICANT_COUNT_KEY_PREFIX + postId + ":applicantCount";
    }

    public List<Object> getValues(List<String> keys) {
        if (keys == null || keys.isEmpty()) {
            return new ArrayList<>();
        }
        return redisTemplate.opsForValue().multiGet(keys);
    }

    public void setValues(Map<String, String> cacheData) {
        if (cacheData.isEmpty()) {
            return;
        }
        redisTemplate.opsForValue().multiSet(cacheData);

    }
}
