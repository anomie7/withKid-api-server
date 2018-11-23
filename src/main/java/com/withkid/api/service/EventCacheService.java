package com.withkid.api.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.stereotype.Service;

import com.withkid.api.domain.EventCacheDto;
import com.withkid.api.domain.SearchVO;

@Service
public class EventCacheService {
	@Resource(name = "redisTemplate")
	private ListOperations<String, EventCacheDto> listOperation;
	
	public List<EventCacheDto> search(SearchVO search, Pageable pageable) {
		Integer firstIdx = pageable.getPageNumber() * pageable.getPageSize();
		Integer lastIdx = firstIdx + pageable.getPageSize() - 1;
		List<EventCacheDto> list = listOperation.range(search.getKey(), firstIdx, lastIdx);
		return list;
	}

}
