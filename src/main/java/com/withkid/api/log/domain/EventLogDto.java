package com.withkid.api.log.domain;

import lombok.*;

import java.io.Serializable;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventLogDto implements Serializable{
		private Long eventId;
		private String interparkCode;
		private String name;
		private String location;
		private String startDate;
		private String endDate;
		private EventType kindOf;
		private String imageFilePath;
}
