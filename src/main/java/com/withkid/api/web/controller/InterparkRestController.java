package com.withkid.api.web.controller;

import com.withkid.api.domain.InterParkContent;
import com.withkid.api.dto.EventCacheDto;
import com.withkid.api.dto.SearchVO;
import com.withkid.api.service.EventCacheService;
import com.withkid.api.service.InterparkService;
import com.withkid.api.web.response.EventResponse;
import io.lettuce.core.RedisException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor

@CrossOrigin(origins = "*")
@RestController
public class InterparkRestController {

	private EventCacheService cacheService;
	private InterparkService interparkService;

	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page.") })
	@GetMapping("/event")
	public ResponseEntity<EventResponse> getEvent(SearchVO search, @ApiIgnore Pageable pageable) {
		List<EventCacheDto> searchEvent;
		String key = search.getKey();

		EventResponse res = null;
		try {
			if (cacheService.isExist(key)) {
				searchEvent = cacheService.search(search, pageable);
			} else {
				searchEvent = cacheService.cacheEvent(pageable, search);
			}
			res = EventResponse.builder().events(searchEvent).build();
			Page<EventCacheDto> page = new PageImpl<>(searchEvent, pageable, cacheService.getTotal(key) );
			res.addPageInfo(page);
		} catch (RedisException e) {
			Page<InterParkContent> list = interparkService.searchEvent(search, pageable);
			searchEvent = list.stream().map(EventCacheDto::fromEntity).collect(Collectors.toList());
			res = EventResponse.builder().events(searchEvent).build();
			Page<EventCacheDto> page = new PageImpl<>(searchEvent, pageable, list.getTotalElements() );
			res.addPageInfo(page);
		}

		return ResponseEntity.ok().body(res);
	}
}
