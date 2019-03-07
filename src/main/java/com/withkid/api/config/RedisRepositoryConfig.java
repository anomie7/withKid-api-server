package com.withkid.api.config;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.resource.DefaultClientResources;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableRedisRepositories
public class RedisRepositoryConfig {
	@Value("${spring.redis.host}")
	private String redisHost;

	@Value("${spring.redis.port}")
	private int redisPort;

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        return new RedisStandaloneConfiguration(redisHost, redisPort);
    }

    @Bean
    LettucePoolingClientConfiguration lettucePoolConfig(){
        return LettucePoolingClientConfiguration.builder()
                .poolConfig(new GenericObjectPoolConfig())
                .clientOptions(clientOptions())
                .clientResources(DefaultClientResources.create())
                .build();
    }

    @Bean
    public ClientOptions clientOptions(){
        return ClientOptions.builder()
                .autoReconnect(false)
                .pingBeforeActivateConnection(true)
                .build();
    }

	@Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(redisStandaloneConfiguration(), lettucePoolConfig());
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<byte[], byte[]> redisTemplate = new RedisTemplate<>();
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }
}
