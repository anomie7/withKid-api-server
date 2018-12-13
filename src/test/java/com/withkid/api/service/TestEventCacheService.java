package com.withkid.api.service;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.withkid.api.domain.Address;
import com.withkid.api.domain.InterParkContent;
import com.withkid.api.domain.InterparkType;
import com.withkid.api.dto.EventCacheDto;
import com.withkid.api.dto.SearchVO;
import com.withkid.api.exception.EventNotFountException;
import com.withkid.api.repository.InterParkRepository;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
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

	@Resource
	private InterParkRepository interparkRepository;

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
				+ keyword.getStartDate().toLocalDate().toString() + "::"
				+ keyword.getEndDate().toLocalDate().toString(), keyword.getKey());
	}

	@Test
	public void setExpireKeyTest() throws InterruptedException {
		listOperation.leftPush(keyword.getKey(), EventCacheDto.builder().build());
		redisTemplate.expireAt(keyword.getKey(), Date.from(ZonedDateTime.now().plusSeconds(30).toInstant()));
		Thread.sleep(35000);
		Boolean isExist = redisTemplate.hasKey(keyword.getKey());
		assertEquals(false, isExist);
	}

	@Test
	public void saveTestWhenKeyNotExist() {
		// given
		InterParkContent obj1 = InterParkContent.builder().name("뽀로로1").address(Address.builder().city("서울특별시").build())
				.dtype(InterparkType.Mu).startDate(LocalDateTime.now().plusDays(1))
				.endDate(LocalDateTime.now().plus(Period.ofDays(1))).build();
		InterParkContent obj2 = InterParkContent.builder().name("뽀로로2").address(Address.builder().city("서울시").build())
				.dtype(InterparkType.Cl).startDate(LocalDateTime.now().plusDays(1))
				.endDate(LocalDateTime.now().plus(Period.ofDays(2))).build();
		InterParkContent obj3 = InterParkContent.builder().name("뽀로로3").address(Address.builder().city("서울").build())
				.dtype(InterparkType.Pl).startDate(LocalDateTime.now().plusDays(1))
				.endDate(LocalDateTime.now().minusDays(2)).build();
		List<InterParkContent> testLs = new ArrayList<>();
		testLs.add(obj1);
		testLs.add(obj3);
		testLs.add(obj2);

		interparkRepository.saveAll(testLs);

		String city = "서울";
		InterparkType dtype = InterparkType.Mu;
		LocalDateTime start = LocalDate.now().atStartOfDay();
		LocalDateTime end = start.plusDays(7);
		SearchVO search = SearchVO.builder().region(city).kindOf(dtype).startDate(start).endDate(end).build();
		Pageable pageable = PageRequest.of(0, 10);

		// when
		List<EventCacheDto> res = eventCacheService.cacheEvent(pageable, search);

		// then
		res.forEach(obj -> {
			DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			LocalDateTime $start = LocalDate.parse(obj.getStartDate(), ofPattern).atStartOfDay();
			LocalDateTime $end = LocalDate.parse(obj.getEndDate(), ofPattern).atStartOfDay();
			assertEquals(obj.getKindOf(), search.getKindOf());
			assertEquals(true, $start.isAfter(start));
			assertEquals(true, $end.isBefore(end));
		});

		redisTemplate.delete(search.getKey());
	}

	@Test(expected=EventNotFountException.class)
	public void cacheEventTestWhenResultEqNull() {
		// given
		String city = "대전";
		InterparkType dtype = InterparkType.Mu;
		LocalDateTime start = LocalDate.now().atStartOfDay();
		LocalDateTime end = start.plusDays(7);
		SearchVO search = SearchVO.builder().region(city).kindOf(dtype).startDate(start).endDate(end).build();
		Pageable pageable = PageRequest.of(0, 10);

		// when
		eventCacheService.cacheEvent(pageable, search);

		// then
		redisTemplate.delete(search.getKey());
	}

	@Test
	public void saveTestWhenKeyExist() {
		// given
		InterParkContent obj1 = InterParkContent.builder().name("뽀로로1").address(Address.builder().city("서울특별시").build())
				.dtype(InterparkType.Mu).startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plus(Period.ofDays(1))).build();
		InterParkContent obj2 = InterParkContent.builder().name("뽀로로2").address(Address.builder().city("서울시").build())
				.dtype(InterparkType.Cl).startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plus(Period.ofDays(2))).build();
		InterParkContent obj3 = InterParkContent.builder().name("뽀로로3").address(Address.builder().city("서울").build())
				.dtype(InterparkType.Pl).startDate(LocalDateTime.now()).endDate(LocalDateTime.now().minusDays(2))
				.build();
		List<InterParkContent> testLs = new ArrayList<>();
		testLs.add(obj1);
		testLs.add(obj3);
		testLs.add(obj2);

		interparkRepository.saveAll(testLs);

		String city = "서울";
		InterparkType dtype = null;
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = null;

		SearchVO search = SearchVO.builder().region(city).kindOf(dtype).startDate(start).endDate(end).build();

		Pageable pageable = PageRequest.of(0, 10);
		List<InterParkContent> res = interparkService.searchAllEvent(search);
		List<EventCacheDto> list = res.stream().map(EventCacheDto::fromEntity).collect(Collectors.toList());
		listOperation.rightPushAll(search.getKey(), list);

		// when
		List<EventCacheDto> llist = eventCacheService.search(search, pageable);

		// then
		assertEquals(llist.size(), list.size());
		System.out.println(llist.size());
		redisTemplate.delete(search.getKey());
	}

	@Test
	public void getTotalTest() {
		// given
		InterParkContent obj1 = InterParkContent.builder().name("뽀로로1").address(Address.builder().city("서울특별시").build())
				.dtype(InterparkType.Mu).startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plus(Period.ofDays(1))).build();
		InterParkContent obj2 = InterParkContent.builder().name("뽀로로2").address(Address.builder().city("서울시").build())
				.dtype(InterparkType.Cl).startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plus(Period.ofDays(2))).build();
		InterParkContent obj3 = InterParkContent.builder().name("뽀로로3").address(Address.builder().city("서울").build())
				.dtype(InterparkType.Pl).startDate(LocalDateTime.now()).endDate(LocalDateTime.now().minusDays(2))
				.build();
		List<InterParkContent> testLs = new ArrayList<>();
		testLs.add(obj1);
		testLs.add(obj3);
		testLs.add(obj2);

		interparkRepository.saveAll(testLs);

		String city = "서울";
		InterparkType dtype = InterparkType.Mu;
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(7);

		SearchVO search = SearchVO.builder().region(city).kindOf(dtype).startDate(start).endDate(end).build();

		List<InterParkContent> res = interparkService.searchAllEvent(search);
		List<EventCacheDto> list = res.stream().map(EventCacheDto::fromEntity).collect(Collectors.toList());
		listOperation.rightPushAll(search.getKey(), list);

		// when
		Long size = eventCacheService.getTotal(search.getKey());

		// then
		assertEquals(Long.valueOf(list.size()), size);
		redisTemplate.delete(search.getKey());
	}
}
