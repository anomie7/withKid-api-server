package com.withkid.api.web.controller;

import java.beans.EventSetDescriptor;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/eventLog")
	public ResponseEntity<Set<EventLogDto>> findLatestEventLogs(@RequestHeader(name="Authorization") String accessToken){
		String key = eventLogService.getKey(accessToken);
		Set<EventLogDto> body = eventLogService.findLast10Logs(key);
		return ResponseEntity.ok().body(body);
	}
	
	@PostMapping("/eventLog")
	public ResponseEntity<String> addEventLog(@RequestHeader(name="Authorization") String accessToken,
											  @RequestBody EventLogDto eventLog) {
		eventLogService.saveByAccessToken(accessToken, eventLog);
		return ResponseEntity.ok().body("log stored");
	}
	
}
