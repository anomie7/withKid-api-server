package com.withkid.api.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventCacheDto {
	private Long eventId;
	private String name;
	private String location;
	private String startDate;
	private String endDate;
	private InterparkType kindOf;
	private String imageFilePath;
	@Builder.Default
	private List<PriceDto> price = new ArrayList<>();

	public static EventCacheDto fromEntity(InterParkData entity) {
		EventCacheDto dto = EventCacheDto.builder()
				.eventId(entity.getId())
				.name(entity.getName())
				.location(entity.getLocation())
				.startDate(entity.getStartDate().toLocalDate().toString())
				.endDate(entity.getEndDate().toLocalDate().toString())
				.kindOf(entity.getDtype())
				.imageFilePath(entity.getImageFilePath())
				.build();
		dto.addPrice(entity.getPrice());
		return dto;
	}

	private void addPrice(List<Price> price) {
		this.price = price.stream().map(PriceDto::fromEntity).collect(Collectors.toList());
	}
}