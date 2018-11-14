package com.withkid.api.log.service;

import java.util.Optional;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import com.withkid.api.log.domain.EventLogDto;


@Service
public class EventLogService {
	@Resource(name = "redisTemplate")
	private ZSetOperations<String, EventLogDto> zsetOperations;

	public Integer findMaxScore(String key) {
		Optional<TypedTuple<EventLogDto>> tupleOpt = zsetOperations.rangeWithScores(key, -1, -1).stream().findFirst();
		Integer maxScore = 0;
		if (tupleOpt.isPresent()) {
			maxScore = tupleOpt.get().getScore().intValue();
			return maxScore + 1;
		}
		return maxScore;
	}

	public Set<EventLogDto> findLast10Logs(String testKey) {
		return zsetOperations.reverseRange(testKey, 0, 9);
	}

	public void save(String key, EventLogDto event) {
		zsetOperations.add(key, event, findMaxScore(key));
	}
}
