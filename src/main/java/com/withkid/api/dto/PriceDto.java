package com.withkid.api.dto;

import com.withkid.api.domain.Price;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
public class PriceDto {
	private Long id;
	private String name;
	private int price;
	private boolean defaultPrice;
	private String ticketInfo;
	private String extraInfo;

	public static PriceDto fromEntity(Price price) {
		return PriceDto.builder().id(price.getId()).name(price.getName())
				.price(price.getPrice()).defaultPrice(price.isDefaultPrice())
				.ticketInfo(price.getTicketInfo()).extraInfo(price.getExtraInfo())
				.build();
	}
}
