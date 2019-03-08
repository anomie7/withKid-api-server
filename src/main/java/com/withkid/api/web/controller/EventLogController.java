package com.withkid.api.web.controller;

import com.withkid.api.log.domain.EventLogDto;
import com.withkid.api.log.service.EventLogService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@AllArgsConstructor
@CrossOrigin(origins="*")
@RestController
public class EventLogController {
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
