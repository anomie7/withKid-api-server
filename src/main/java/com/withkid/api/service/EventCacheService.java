package com.withkid.api.service;

import com.withkid.api.domain.InterParkContent;
import com.withkid.api.dto.EventCacheDto;
import com.withkid.api.dto.SearchVO;
import com.withkid.api.exception.EventNotFountException;
import io.lettuce.core.RedisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.PoolException;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventCacheService {

	@Resource(name = "redisTemplate")
	private ListOperations<String, EventCacheDto> listOperation;
	private RedisTemplate<String, EventCacheDto> redisTemplate;
	private InterparkService interparkService;

	@Autowired
	public EventCacheService(RedisTemplate<String, EventCacheDto> redisTemplate, InterparkService interparkService) {
		this.redisTemplate = redisTemplate;
		this.interparkService = interparkService;
	}

	public List<EventCacheDto> search(SearchVO search, Pageable pageable) {
		Integer firstIdx = pageable.getPageNumber() * pageable.getPageSize();
		Integer lastIdx = getLastIndex(pageable);
		List<EventCacheDto> list = listOperation.range(search.getKey(), firstIdx, lastIdx);
		return list;
	}

	public List<EventCacheDto> cacheEvent(Pageable pageable, SearchVO search) {
		List<InterParkContent> list = interparkService.searchAllEvent(search);
		
		if(list.isEmpty()) {
			throw new EventNotFountException();
		}
		
		List<EventCacheDto> cacheList = list.stream().map(EventCacheDto::fromEntity).collect(Collectors.toList());
		listOperation.rightPushAll(search.getKey(), cacheList);
		redisTemplate.expireAt(search.getKey(), Date.from(ZonedDateTime.now().plusWeeks(1).toInstant()));
		if(cacheList.size() > pageable.getPageSize()) {
			return cacheList.subList(getFirstIndex(pageable), getLastIndex(pageable) + 1);
		}
		return cacheList;
	}

	public Integer getFirstIndex(Pageable pageable) {
		return pageable.getPageNumber() * pageable.getPageSize();
	}

	public Integer getLastIndex(Pageable pageable) {
		return getFirstIndex(pageable) + pageable.getPageSize() - 1;
	}

	public boolean isExist(String key) {
		Boolean hasKey = null;
		try {
			hasKey = redisTemplate.hasKey(key);
		} catch (PoolException e) {
			throw new RedisException("RedisException!");
		} catch (RedisSystemException e) {
			throw new RedisException("RedisException!");
		}
		return hasKey;
	}

	public Long getTotal(String key) {
		return listOperation.size(key);
	}
}
