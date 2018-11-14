package com.withkid.api.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.withkid.api.domain.InterParkData;
import com.withkid.api.domain.InterparkType;

public interface InterParkRepository
		extends JpaRepository<InterParkData, Long>, QuerydslPredicateExecutor<InterParkData> {
	public List<InterParkData> findAllByDtype(InterparkType dtype);

	public List<InterParkData> findByEndDateBefore(LocalDateTime now);

	public List<InterParkData> findByEndDateAfter(LocalDateTime now);

	@Query("select i.interparkCode from InterParkData i where i.dtype = :dtype")
	public List<String> findInterparkcodeByDtype(@Param("dtype") InterparkType dtype);
}
