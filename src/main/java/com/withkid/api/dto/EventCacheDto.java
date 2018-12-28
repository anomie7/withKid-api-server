package com.withkid.api.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.withkid.api.domain.InterParkContent;
import com.withkid.api.domain.InterparkType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@JsonTypeName("EventCacheDto")
@Getter 
@NoArgsConstructor
public class EventCacheDto extends AbstractEventDto{
	private String startDate;
	private String endDate;
	
	@Builder
	public EventCacheDto(Long eventId, String interparkCode,String name, String location, InterparkType kindOf, String imageFilePath,
			List<PriceDto> price, String startDate, String endDate) {
		super(eventId, interparkCode, name, location, kindOf, imageFilePath, price);
		this.startDate = startDate;
		this.endDate = endDate;
	}
	
	public static EventCacheDto fromEntity(InterParkContent entity) {
		EventCacheDto dto = EventCacheDto.builder()
				.eventId(entity.getId())
				.interparkCode(entity.getInterparkCode())
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
}