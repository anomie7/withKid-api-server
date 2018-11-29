package com.withkid.api.repository;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.withkid.api.domain.Address;
import com.withkid.api.domain.DeleteFlag;
import com.withkid.api.domain.InterParkData;
import com.withkid.api.domain.InterparkType;
import com.withkid.api.domain.Price;
import com.withkid.api.domain.QInterParkData;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TestQuerydsl {
	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private InterParkRepository interparkRepository;
	private List<InterParkData> testLs = new ArrayList<>();
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

		InterParkData obj1 = InterParkData.builder().name("뽀로로1").address(Address.builder().city("서울특별시").build())
				.dtype(InterparkType.Mu).startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plus(Period.ofDays(1))).build();
		InterParkData obj2 = InterParkData.builder().name("뽀로로2").address(Address.builder().city("서울시").build())
				.dtype(InterparkType.Cl).startDate(LocalDateTime.now())
				.endDate(LocalDateTime.now().plus(Period.ofDays(2))).build();
		InterParkData obj3 = InterParkData.builder().name("뽀로로3").address(Address.builder().city("서울").build())
				.dtype(InterparkType.Pl).startDate(LocalDateTime.now()).endDate(LocalDateTime.now().minusDays(2))
				.build();
		InterParkData obj4 = InterParkData.builder().name("뽀로로4").address(Address.builder().city("대구광역시").build())
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
	public void testNomalQuery() {
		QInterParkData data = QInterParkData.interParkData;
		JPAQuery query = new JPAQuery(em);
		query.from(data).where(data.deleteflag.eq(DeleteFlag.N)).orderBy(data.startDate.asc());
		List<InterParkData> ls = query.fetch();

		assertEquals(prices4.size(), ls.get(0).getPrice().size());
		assertEquals(prices.size(), ls.get(1).getPrice().size());
		assertEquals(prices3.size(), ls.get(2).getPrice().size());
		assertEquals(prices2.size(), ls.get(3).getPrice().size());
	}

	@Test
	public void testUpdateQuery() {
		QInterParkData data = QInterParkData.interParkData;
		JPAQuery query = new JPAQuery(em);

		new JPAUpdateClause(em, data).where(data.name.eq("뽀로로1")).set(data.name, "뽀통령").execute();
		query.select(data.name).from(data).where(data.name.eq("뽀통령"));
		Object obj = query.fetchOne();
		assertEquals("뽀통령", obj.toString());
	}

}
