package com.withkid.api.log.domain;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventLogDto implements Serializable{
		private Long eventId;
		private String name;
		private String location;
		private String startDate;
		private String endDate;
		private EventType kindOf;
		private String imageFilePath;
}
