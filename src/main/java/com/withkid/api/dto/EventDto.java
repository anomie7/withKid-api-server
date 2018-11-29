package com.withkid.api.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.withkid.api.domain.InterParkData;
import com.withkid.api.domain.InterparkType;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@JsonTypeName("EventDto")
@Getter
@NoArgsConstructor
public class EventDto extends AbstractEventDto {
	private LocalDate startDate;
	private LocalDate endDate;

	@Builder
	public EventDto(Long eventId, String name, String location, InterparkType kindOf, String imageFilePath,
			List<PriceDto> price, LocalDate startDate, LocalDate endDate) {
		super(eventId, name, location, kindOf, imageFilePath, price);
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public static EventDto fromEntity(InterParkData entity) {
		EventDto dto = EventDto.builder().eventId(entity.getId()).name(entity.getName()).location(entity.getLocation())
				.startDate(entity.getStartDate().toLocalDate()).endDate(entity.getEndDate().toLocalDate())
				.kindOf(entity.getDtype()).imageFilePath(entity.getImageFilePath()).build();
		dto.addPrice(entity.getPrice());
		return dto;
	}
}
