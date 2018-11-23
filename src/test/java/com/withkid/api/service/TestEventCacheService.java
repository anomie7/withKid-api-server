package com.withkid.api.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

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

import com.withkid.api.domain.EventDto;
import com.withkid.api.domain.InterparkType;
import com.withkid.api.domain.SearchVO;
import com.withkid.api.web.response.EventResponse;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEventCacheService {
	@Autowired
	private EventCacheService eventChacheService;

	@Resource(name = "redisTemplate")
	private ListOperations<String, EventDto> listOperation;

	@Resource
	private RedisTemplate<String, EventDto> redisTemplate;
	
	@Resource
	private InterparkService interparkService;

	SearchVO keyword = SearchVO.builder().region("서울").startDate(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusDays(1)).kindOf(InterparkType.Mu).build();

	@Test
	public void existTest() {
		listOperation.leftPush(keyword.getKey(), EventDto.builder().build());
		boolean isExist = redisTemplate.hasKey(keyword.getKey());
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
		listOperation.leftPush(keyword.getKey(), EventDto.builder().build());
		redisTemplate.expireAt(keyword.getKey(),Date.from(ZonedDateTime.now().plusSeconds(30).toInstant()));
		Thread.sleep(35000);
		Boolean isExist = redisTemplate.hasKey(keyword.getKey());
		assertEquals(false, isExist);
	}

	@Test
	public void saveTest() {
		String city = "서울";
		InterparkType dtype = InterparkType.Mu;
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(7);

		SearchVO search = SearchVO.builder().region(city).kindOf(dtype).startDate(start).endDate(end)
				.build();

		Pageable pageable = PageRequest.of(0, 10);
		EventResponse response = interparkService.searchEvent(search, pageable); //EventResponse는 controller에서 반환하는 타입
		
		List<EventDto> lists = response.getEvents();
		listOperation.rightPushAll(search.getKey(), lists);
		List<EventDto> llist = listOperation.range(search.getKey(), 0, listOperation.size(search.getKey()));
		redisTemplate.delete(search.getKey());
	}
}
