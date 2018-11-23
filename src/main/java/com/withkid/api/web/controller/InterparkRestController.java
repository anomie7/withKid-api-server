package com.withkid.api.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.withkid.api.domain.EventCacheDto;
import com.withkid.api.domain.SearchVO;
import com.withkid.api.service.EventCacheService;
import com.withkid.api.web.response.EventResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import springfox.documentation.annotations.ApiIgnore;

@CrossOrigin(origins="*")
@RestController
public class InterparkRestController {
	
	@Autowired
	private EventCacheService cacheService;
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
		@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page.") })
	@GetMapping("/event")
	public ResponseEntity<EventResponse> getEvent(SearchVO search, @ApiIgnore Pageable pageable){
		List<EventCacheDto> searchEvent;
		if(cacheService.isExist(search.getKey())) {
			searchEvent = cacheService.search(search, pageable);
		}else {
			searchEvent = cacheService.cacheEvent(pageable, search);
		}
		EventResponse res = EventResponse.builder().events(searchEvent).build();
		PageImpl<EventCacheDto> page = new PageImpl<>(searchEvent);
		res.addPageInfo(page);
		return ResponseEntity.ok().body(res);
	}
}
