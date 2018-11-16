package com.withkid.api.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.withkid.api.log.domain.EventLogDto;
import com.withkid.api.log.service.EventLogService;
import com.withkid.api.log.service.JwtService;

@CrossOrigin(origins="*")
@RestController
public class EventLogController {
	@Autowired
	private EventLogService eventLogService;
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/eventLog")
	public ResponseEntity<String> addEventLog(@RequestHeader(name="Authorization") String accessToken,
											  @RequestBody EventLogDto eventLog) {
		String key = jwtService.getKey(accessToken);
		eventLogService.save(key, eventLog);
		return ResponseEntity.ok().body("log stored");
	}
}
