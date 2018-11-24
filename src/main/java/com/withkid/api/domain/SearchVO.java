package com.withkid.api.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchVO {
	private String region;
	private InterparkType kindOf;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime startDate;

	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
	private LocalDateTime endDate;

	public String getKey() {
		return this.getRegionKey() + "::" + this.getKindOfKey().toString() + "::"
				+ this.getStartDateKey() + "::" + this.getEndDateKey();
	}

	public String getRegionKey() {
		if(region == null) {
			return "전체";
		}
		return region;
	}

	public String getKindOfKey() {
		if(kindOf == null) {
			return "전체";
		}
		return kindOf.toString();
	}

	public String getStartDateKey() {
		if(startDate == null) {
			return "전체기간";
		}
		return startDate.toLocalDate().toString();
	}

	public String getEndDateKey() {
		if(endDate == null) {
			return "전체기간";
		}
		return endDate.toLocalDate().toString();
	}
	
}
