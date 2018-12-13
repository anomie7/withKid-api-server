package com.withkid.api.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.withkid.api.domain.DeleteFlag;
import com.withkid.api.domain.InterparkType;
import com.withkid.api.domain.QInterParkContent;
import com.withkid.api.dto.SearchVO;

public class InterparkPredicateProvider {
	public static Predicate getSearchPredicate(SearchVO search) {
		Optional<String> cityOpt = Optional.ofNullable(search.getRegion());
		Optional<InterparkType> dtypeOpt = Optional.ofNullable(search.getKindOf());
		Optional<LocalDateTime> startOpt = Optional.ofNullable(search.getStartDate());
		Optional<LocalDateTime> endOpt = Optional.ofNullable(search.getEndDate());
		
		BooleanBuilder build = new BooleanBuilder();
		QInterParkContent data = QInterParkContent.interParkContent;
		cityOpt.ifPresent(city -> build.and(data.address.city.contains(city)));
		dtypeOpt.ifPresent(kind -> build.and(data.dtype.eq(kind)));

		if (startOpt.isPresent() && endOpt.isPresent()) {
			build.and(data.startDate.before(endOpt.get())
					.and(data.endDate.after(startOpt.get())));
		}

		build.and(data.deleteflag.eq(DeleteFlag.N));

		return build;
	}
}
