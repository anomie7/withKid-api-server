package com.withkid.api.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.withkid.api.domain.SearchVO;
import com.withkid.api.service.InterparkService;
import com.withkid.api.web.response.EventResponse;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import springfox.documentation.annotations.ApiIgnore;

@CrossOrigin(origins="*")
@RestController
public class InterparkRestController {
	@Autowired
	private InterparkService interpakrService;
	
	@ApiImplicitParams({
		@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)"),
		@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page.") })
	@GetMapping("/event")
	public ResponseEntity<EventResponse> getEvent(SearchVO search, @ApiIgnore Pageable pageable){
		EventResponse searchEvent = interpakrService.searchEvent(search, pageable);
		return ResponseEntity.ok().body(searchEvent);
	}
}
