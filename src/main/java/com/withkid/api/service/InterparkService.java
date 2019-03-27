package com.withkid.api.service;

import com.google.common.collect.Lists;
import com.withkid.api.domain.InterParkContent;
import com.withkid.api.dto.SearchVO;
import com.withkid.api.repository.InterParkRepository;
import com.withkid.api.repository.InterparkPredicateProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class InterparkService {
	
	@Autowired
	private InterParkRepository interparkRepository;

	@Transactional(readOnly=true)
	public Page<InterParkContent> searchEvent(SearchVO search, Pageable pageable) {
		Page<InterParkContent> event = interparkRepository.findAll(InterparkPredicateProvider.getSearchPredicate(search)
																,pageable);
		return event;
	}
	
	@Transactional(readOnly=true)
	public List<InterParkContent> searchAllEvent(SearchVO search) {
		Iterable<InterParkContent> event = interparkRepository.findAll(InterparkPredicateProvider.getSearchPredicate(search));
		return Lists.newArrayList(event);
	}

	@Transactional(readOnly=true)
	public List<InterParkContent> findAllByIds(List<Long> ids) {
		Iterable<InterParkContent> event = interparkRepository.findAll(InterparkPredicateProvider.getEventPredicate(ids));
		List<InterParkContent> result = ids.stream().map(id ->
				Lists.newArrayList(event).stream().filter(content -> content.getId().equals(id)).findFirst().orElse(null)
		).filter(Objects::nonNull).collect(Collectors.toList());
		return result;
	}
}
