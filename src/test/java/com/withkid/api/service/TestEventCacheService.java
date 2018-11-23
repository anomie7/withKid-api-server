package com.withkid.api.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.withkid.api.domain.EventCacheDto;
import com.withkid.api.domain.InterParkData;
import com.withkid.api.domain.InterparkType;
import com.withkid.api.domain.SearchVO;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class TestEventCacheService {
	@Autowired
	private EventCacheService eventCacheService;

	@Resource(name = "redisTemplate")
	private ListOperations<String, EventCacheDto> listOperation;

	@Resource
	private RedisTemplate<String, EventCacheDto> redisTemplate;
	
	@Resource
	private InterparkService interparkService;

	SearchVO keyword = SearchVO.builder().region("서울").startDate(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusDays(1)).kindOf(InterparkType.Mu).build();

	@Test
	public void existTest() {
		listOperation.leftPush(keyword.getKey(), EventCacheDto.builder().build());
		boolean isExist = eventCacheService.isExist(keyword.getKey());
		assertEquals(true, isExist);
		redisTemplate.delete(keyword.getKey());
	}

	@Test
	public void getKeyTest() {
		assertEquals(keyword.getRegion() + "::" + keyword.getKindOf().toString() + "::"
				+ keyword.getStartDate().toString() + "::" + keyword.getEndDate().toString(), keyword.getKey());
	}

	@Test
	public void setExpireKeyTest() throws InterruptedException {
		listOperation.leftPush(keyword.getKey(), EventCacheDto.builder().build());
		redisTemplate.expireAt(keyword.getKey(),Date.from(ZonedDateTime.now().plusSeconds(30).toInstant()));
		Thread.sleep(35000);
		Boolean isExist = redisTemplate.hasKey(keyword.getKey());
		assertEquals(false, isExist);
	}

	@Test
	public void saveTestWhenKeyNotExist() {
		//given
		String city = "서울";
		InterparkType dtype = InterparkType.Mu;
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(7);
		SearchVO search = SearchVO.builder().region(city).kindOf(dtype).startDate(start).endDate(end)
				.build();
		Pageable pageable = PageRequest.of(0, 10);
		
		//when
		List<EventCacheDto> res = eventCacheService.cacheEvent(pageable, search);
		
		//then
		assertEquals(res.size(), pageable.getPageSize());
		
		redisTemplate.delete(search.getKey());
	}
	
	@Test
	public void saveTestWhenKeyExist() {
		//given
		String city = "서울";
		InterparkType dtype = InterparkType.Mu;
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(7);

		SearchVO search = SearchVO.builder().region(city).kindOf(dtype).startDate(start).endDate(end)
				.build();

		Pageable pageable = PageRequest.of(1, 10); 
		List<InterParkData> res = interparkService.searchAllEvent(search); 
		List<EventCacheDto> list = res.stream().map(EventCacheDto::fromEntity).collect(Collectors.toList());
		listOperation.rightPushAll(search.getKey(), list);
		
		//when
		List<EventCacheDto> llist = eventCacheService.search(search, pageable);
		
		//then
		assertEquals(llist.size(), pageable.getPageSize());
		
		redisTemplate.delete(search.getKey());
	}
}
