package com.withkid.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.withkid.api.domain.InterParkData;
import com.withkid.api.domain.SearchVO;
import com.withkid.api.repository.InterParkRepository;
import com.withkid.api.repository.InterparkPredicateProvider;
import com.withkid.api.web.response.EventResponse;

@Service
public class InterparkService {
	
	@Autowired
	private InterParkRepository interparkRepository;
	
	@Transactional(readOnly=true)
	public EventResponse searchEvent(SearchVO search, Pageable pageable) {
		Page<InterParkData> event = interparkRepository.findAll(InterparkPredicateProvider.getSearchPredicate(search)
																,pageable);
		EventResponse res = EventResponse.fromEntity(event.getContent());
		res.addPageInfo(event);
		return res;
	}

}
