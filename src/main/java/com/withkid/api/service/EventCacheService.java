package com.withkid.api.service;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.withkid.api.domain.InterParkData;
import com.withkid.api.dto.EventCacheDto;
import com.withkid.api.dto.SearchVO;

@Service
public class EventCacheService {
	@Resource(name = "redisTemplate")
	private ListOperations<String, EventCacheDto> listOperation;
	@Resource
	private RedisTemplate<String, EventCacheDto> redisTemplate;
	@Resource
	private InterparkService interparkService;

	public List<EventCacheDto> search(SearchVO search, Pageable pageable) {
		Integer firstIdx = pageable.getPageNumber() * pageable.getPageSize();
		Integer lastIdx = getLastIndex(pageable);
		List<EventCacheDto> list = listOperation.range(search.getKey(), firstIdx, lastIdx);
		return list;
	}

	public List<EventCacheDto> cacheEvent(Pageable pageable, SearchVO search) {
		List<InterParkData> list = interparkService.searchAllEvent(search);
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
		return redisTemplate.hasKey(key);
	}

	public Long getTotal(String key) {
		return listOperation.size(key);
	}
}
