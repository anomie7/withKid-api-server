package com.withkid.api.log;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.context.junit4.SpringRunner;

import com.withkid.api.log.domain.EventLogDto;
import com.withkid.api.log.service.EventLogService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEventLogService {

	@Resource(name = "redisTemplate")
	private ZSetOperations<String, EventLogDto> zsetOperations;

	@Resource(name = "redisTemplate")
	private RedisTemplate<String, EventLogDto> redisTemplate;

	@Resource
	private EventLogService eventLogService;

	private Long userId = 1L;
	private String userGroup = "userGroup" + userId / 1000 + ":";
	private String testKey = "test" + userGroup + userId;
	private final Integer END_NUM = 100;

	@Before
	public void createDummyData() {
		for (int i = 0; i < END_NUM; i++) {
			zsetOperations.add(testKey, EventLogDto.builder().eventId(1L).name("뽀로로" + i).build(), i);
		}
	}

	@Test
	public void testGetMaxScore() {
		Integer maxScore = eventLogService.findMaxScore(testKey);
		assertEquals("cant found max score value!", true, maxScore.equals(END_NUM));
	}

	@Test
	public void testSave() {
		EventLogDto saveData = EventLogDto.builder().eventId(100L).name("한국사람").build();
		eventLogService.save(testKey, saveData);
		Set<EventLogDto> re = zsetOperations.range(testKey, -1, -1);
		assertEquals(saveData.getEventId(), re.iterator().next().getEventId());
	}

	@Test
	public void testfindLast10Log() {
		Set<EventLogDto> res = eventLogService.findLast10Logs(testKey);
		assertEquals(10, res.size());
	}

	@After
	public void removeMembers() {
		redisTemplate.delete(testKey);
	}
}
