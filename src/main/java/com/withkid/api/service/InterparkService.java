package com.withkid.api.service;

import java.util.List;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withkid.api.domain.InterParkData;
import com.withkid.api.domain.SearchVO;
import com.withkid.api.repository.InterParkRepository;
import com.withkid.api.repository.InterparkPredicateProvider;

@Service
public class InterparkService {
	
	@Autowired
	private InterParkRepository interparkRepository;
	
	@Transactional(readOnly=true)
	public Page<InterParkData> searchEvent(SearchVO search, Pageable pageable) {
		Page<InterParkData> event = interparkRepository.findAll(InterparkPredicateProvider.getSearchPredicate(search)
																,pageable);
		return event;
	}

	public List<InterParkData> searchAllEvent(SearchVO search) {
		Iterable<InterParkData> event = interparkRepository.findAll(InterparkPredicateProvider.getSearchPredicate(search));
		return Lists.newArrayList(event);
	}

}
