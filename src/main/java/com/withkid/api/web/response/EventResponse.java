package com.withkid.api.web.response;

import java.util.List;
import java.util.stream.Collectors;

import com.withkid.api.domain.InterParkContent;
import com.withkid.api.dto.EventDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventResponse extends PageResponse<InterParkContent> {
	private List<?> events;
	private int status;
	private String msg;

	public static EventResponse fromEntity(List<InterParkContent> entityLs) {
		List<EventDto> events = entityLs.stream().map(EventDto::fromEntity).collect(Collectors.toList());
		EventResponse re = EventResponse.builder().events(events).build();
		return re;
	}
}
