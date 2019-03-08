package com.withkid.api.log.service;

import com.withkid.api.log.domain.EventLogDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;
import java.util.Set;


@Service
public class EventLogService {
	@Resource(name = "redisTemplate")
	private ZSetOperations<String, EventLogDto> zsetOperations;
	
	@Resource
	private JwtService jwtService;
	
	public Integer findMaxScore(String key) {
		Optional<TypedTuple<EventLogDto>> tupleOpt = zsetOperations.rangeWithScores(key, -1, -1).stream().findFirst();
		Integer maxScore = 0;
		if (tupleOpt.isPresent()) {
			maxScore = tupleOpt.get().getScore().intValue();
			return maxScore + 1;
		}
		return maxScore;
	}

	public Set<EventLogDto> findLast10Logs(String key) {
		return zsetOperations.reverseRange(key, 0, 9);
	}

	public String getKey(String accessToken) {
		Jws<Claims> body = jwtService.thisAccessTokenUsable(accessToken);
		Integer userId = (Integer) body.getBody().get("user-id");
		String key = "eventLog::" + userId;
		return key;
	}
	
	public void saveByAccessToken(String accessToken, EventLogDto eventLog) {
		String key = getKey(accessToken);
		save(key, eventLog);
	}
	
	public void save(String key, EventLogDto event) {
		zsetOperations.add(key, event, findMaxScore(key));
	}
}
