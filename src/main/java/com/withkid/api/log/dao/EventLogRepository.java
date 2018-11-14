package com.withkid.api.log.dao;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ZSetOperations;

import com.withkid.api.log.domain.EventLogDto;


public class EventLogRepository {
	@Resource(name = "redisTemplate")
	private ZSetOperations<String, EventLogDto> zsetOperations;
}
