package com.withkid.api.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.withkid.api.domain.EventDto;
import com.withkid.api.domain.InterparkType;
import com.withkid.api.domain.SearchVO;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class TestEventCacheService {
	@Autowired
	private EventCacheService eventChacheService;

	@Resource(name = "redisTemplate")
	private ListOperations<String, EventDto> listOperation;

	@Resource
	private RedisTemplate<String, EventDto> redisTemplate;

	SearchVO keyword = SearchVO.builder().region("서울").startDate(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusDays(1)).kindOf(InterparkType.Mu).build();

	@Test
	public void existTest() {
	}

	@Test
	public void getKeyTest() {
	}

	@Test
	public void setExpireKeyTest() {

	}

	@Test
	public void saveTest() {

	}
}
