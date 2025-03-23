package vttp.batch_b.min_project.server.configs;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class RedisConfig {
    private final Logger logger = Logger.getLogger(RedisConfig.class.getName());

   // Get all the redis configuration into the class
   @Value("${spring.data.redis.host}")
   private String redisHost;

   @Value("${spring.data.redis.port}")
   private int redisPort;

   @Value("${spring.data.redis.database}")
   private int redisDatabase;

   @Value("${spring.data.redis.username}")
   private String redisUsername;

   @Value("${spring.data.redis.password}")
   private String redisPassword;

   @Bean("redis-object")
   public RedisTemplate<String, Object> createRedisTemplateObject() {
      
      // Create a database configuration
      RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
      // Sets the database - select 0
      config.setDatabase(redisDatabase);
      // Set the username and password if they are set
      if (!redisUsername.trim().equals("")) {
         logger.info("Setting Redis username and password");
         config.setUsername(redisUsername);
         config.setPassword(redisPassword);
      }

      // Create a connection to the database
      JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

      // Create a factory to connect to Redis
      JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
      jedisFac.afterPropertiesSet();

      // Create the RedisTemplate
      RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
      redisTemplate.setConnectionFactory(jedisFac);
      redisTemplate.setKeySerializer(new StringRedisSerializer());
      redisTemplate.setHashKeySerializer(new StringRedisSerializer());
      return redisTemplate;
   }

   @Bean("redis")
   public RedisTemplate<String, String> createRedisTemplate() {
      
      // Create a database configuration
      RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisHost, redisPort);
      // Sets the database - select 0
      config.setDatabase(redisDatabase);
      // Set the username and password if they are set
      if (!redisUsername.trim().equals("")) {
         logger.info("Setting Redis username and password");
         config.setUsername(redisUsername);
         config.setPassword(redisPassword);
      }

      // Create a connection to the database
      JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();

      // Create a factory to connect to Redis
      JedisConnectionFactory jedisFac = new JedisConnectionFactory(config, jedisClient);
      jedisFac.afterPropertiesSet();

      // Create the RedisTemplate
      RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
      redisTemplate.setConnectionFactory(jedisFac);
      redisTemplate.setKeySerializer(new StringRedisSerializer());
      redisTemplate.setValueSerializer(new StringRedisSerializer());
      redisTemplate.setHashKeySerializer(new StringRedisSerializer());
      redisTemplate.setHashValueSerializer(new StringRedisSerializer());
      return redisTemplate;
   }

   @Bean
    public ObjectMapper objectMapper() {
      ObjectMapper mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      return mapper;
   }
}
