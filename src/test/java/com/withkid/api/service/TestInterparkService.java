package com.withkid.api.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.withkid.api.domain.Address;
import com.withkid.api.domain.InterParkContent;
import com.withkid.api.domain.InterparkType;
import com.withkid.api.domain.Price;
import com.withkid.api.dto.SearchVO;
import com.withkid.api.repository.InterParkRepository;

@RunWith(SpringRunner.class)
@SpringBootTest()
@ActiveProfiles("test")
public class TestInterparkService {
	@Autowired
	private InterparkService interparkService;

	@Autowired
	private InterParkRepository interparkRepository;

	private List<InterParkContent> testLs = new ArrayList<>();
	private List<Price> prices = new ArrayList<>();
	private List<Price> prices2 = new ArrayList<>();
	private List<Price> prices3 = new ArrayList<>();
	private List<Price> prices4 = new ArrayList<>();

	@Before
	public void generateTestObj() {
		prices.add(new Price(null, "일반가", 10000));
		prices.add(new Price(null, "특별가", 20000));
		prices.add(new Price(null, "상상가", 30000));
		prices.add(new Price(null, "부모동반", 6000));

		prices2.add(new Price(null, "일반가2", 20000));
		prices2.add(new Price(null, "특별가2", 40000));
		prices2.add(new Price(null, "상상가2", 60000));
		prices2.add(new Price(null, "부모동반2", 12000));

		prices3.add(new Price(null, "일반가2", 20000));
		prices3.add(new Price(null, "특별가2", 40000));
		prices3.add(new Price(null, "상상가2", 60000));
		prices3.add(new Price(null, "부모동반2", 12000));

		prices4.add(new Price(null, "일반가2", 20000));
		prices4.add(new Price(null, "특별가2", 40000));
		prices4.add(new Price(null, "상상가2", 60000));
		prices4.add(new Price(null, "부모동반2", 12000));

		InterParkContent obj1 = InterParkContent.builder().name("뽀로로1").address(Address.builder().city("서울특별시").build())
				.dtype(InterparkType.Mu).startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plus(Period.ofDays(1))).build();
		InterParkContent obj2 = InterParkContent.builder().name("뽀로로2").address(Address.builder().city("서울시").build())
				.dtype(InterparkType.Cl).startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plus(Period.ofDays(2))).build();
		InterParkContent obj3 = InterParkContent.builder().name("뽀로로3").address(Address.builder().city("서울").build())
				.dtype(InterparkType.Pl).startDate(LocalDateTime.now()).endDate(LocalDateTime.now().minusDays(2))
				.build();
		InterParkContent obj4 = InterParkContent.builder().name("뽀로로4").address(Address.builder().city("대구광역시").build())
				.dtype(InterparkType.Ex).startDate(LocalDateTime.now().minusMonths(2))
				.endDate(LocalDateTime.now().minusDays(1)).build();

		obj1.addPrice(prices);
		obj2.addPrice(prices2);
		obj3.addPrice(prices3);
		obj4.addPrice(prices4);
		testLs.add(obj4);
		testLs.add(obj1);
		testLs.add(obj3);
		testLs.add(obj2);

		interparkRepository.saveAll(testLs);
	}

	@Test
	@Transactional
	public void testSearchEvent() {
		String city = "서울";
		InterparkType dtype = InterparkType.Mu;
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusDays(7);

		SearchVO search = SearchVO.builder().region(city).kindOf(dtype).startDate(start).endDate(end)
				.build();

		Pageable pageable = PageRequest.of(0, 10);
		Page<InterParkContent> event = interparkService.searchEvent(search, pageable);
		event.getContent().stream().forEach( obj -> {
			assertThat(obj.getDtype(), equalTo(dtype));
			assertThat(obj.getAddress().getCity(), containsString(city) );
		});
	}
	
	@Test
	@Transactional
	public void testSearchEventValueEqNull() {
		String city = null;
		InterparkType dtype = null;
		LocalDateTime start = null;
		LocalDateTime end = null;

		SearchVO search = SearchVO.builder().region(city).kindOf(dtype).startDate(start).endDate(end)
				.build();
		
		Pageable pageable = PageRequest.of(0, 10);
		Page<InterParkContent> event = interparkService.searchEvent(search, pageable);
		assertEquals(testLs.size(), event.getNumberOfElements() );
	}

	@Test
	@Transactional
	public void testSearchAllEventWhenResultEqNull() {
		String city = "대전";
		InterparkType dtype = InterparkType.Pl;
		LocalDateTime start = null;
		LocalDateTime end = null;

		SearchVO search = SearchVO.builder().region(city).kindOf(dtype).startDate(start).endDate(end).build();

		List<InterParkContent> event = interparkService.searchAllEvent(search);
		assertEquals(0, event.size());
	}

}
