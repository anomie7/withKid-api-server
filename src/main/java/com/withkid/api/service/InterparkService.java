package com.withkid.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.withkid.api.domain.InterParkContent;
import com.withkid.api.dto.SearchVO;
import com.withkid.api.repository.InterParkRepository;
import com.withkid.api.repository.InterparkPredicateProvider;

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

}
