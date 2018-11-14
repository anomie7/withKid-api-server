package com.withkid.api.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventDto {
	private Long eventId;
	private String name;
	private String location;
	private LocalDate startDate;
	private LocalDate endDate;
	private InterparkType kindOf;
	private String imageFilePath;
	@Builder.Default
	private List<PriceDto> price = new ArrayList<>();

	public static EventDto fromEntity(InterParkData entity) {
		EventDto dto = EventDto.builder()
				.eventId(entity.getId())
				.name(entity.getName())
				.location(entity.getLocation())
				.startDate(entity.getStartDate().toLocalDate())
				.endDate(entity.getEndDate().toLocalDate())
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
