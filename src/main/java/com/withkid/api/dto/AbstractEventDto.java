package com.withkid.api.dto;

import com.withkid.api.domain.InterparkType;
import com.withkid.api.domain.Price;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEventDto {
	private Long eventId;
	private String interparkCode;
	private String name;
	private String location;
	private InterparkType kindOf;
	private String imageFilePath;
	private List<PriceDto> price = new ArrayList<>();
	
	protected void addPrice(List<Price> price) {
		this.price = price.stream().map(PriceDto::fromEntity).collect(Collectors.toList());
	}
}
