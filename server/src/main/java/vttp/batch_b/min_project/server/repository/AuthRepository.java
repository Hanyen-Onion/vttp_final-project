package vttp.batch_b.min_project.server.repository;


import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class AuthRepository {
    
    @Autowired @Qualifier("redis")
    private RedisTemplate<String, String> template;

    // get key
    public String getAccessToken (String key) {
        return template.opsForValue().get(key);
    }

    // set key value
    public void saveAccessToken(String key, String accessToken, int expire) {
        template.opsForValue().set(key, accessToken);

        expiration(key, expire);
    }

    // expire key seconds
    public void expiration(String key, int expire) {
        template.expire(key, Duration.ofSeconds(expire));
    }

    // exists
    public Boolean exists(String key) {
        return template.hasKey(key);
    }

    // ttl
    public String getExpiration(String key) {
        return template.getExpire(key).toString();
    }
}
