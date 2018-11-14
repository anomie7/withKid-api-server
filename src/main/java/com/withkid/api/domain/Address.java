package com.withkid.api.domain;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Getter 
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder @Slf4j
@Embeddable
public class Address {
	private String fullAddress;
	private String province;
	private String city;
	private String district;
	
	public static Address convertStringToAddressObj(String address) {
		
		if(address.isEmpty()) {
			log.debug("address is empty!");
			return null;
		}
		
		Pattern cityPattern = Pattern.compile("[가-힣]{4}시");
		Matcher cityMacher = cityPattern.matcher(address);
		
		Pattern provincePattern = Pattern.compile("^(([가-힣]*[북|남])|([가-힣]{2,}도))");
		Matcher provinceMatcher = provincePattern.matcher(address);
		
		Pattern etcPattern = Pattern.compile("^([가-힣]{2,}시|[가-힣]{2,2}[^동|구])");
		Matcher etcMatcher = etcPattern.matcher(address);
		
		log.debug("추출할 주소 값: {}",address);
		
		String[] tmp = address.split(" ");
		if(cityMacher.find()) {
			return Address.builder()
						  .fullAddress(address)
						  .city(tmp[0])
						  .district(tmp[1])
						  .build();
		}else if(provinceMatcher.find()) {
			return Address.builder()
						  .fullAddress(address)
						  .province(tmp[0])
						  .city(tmp[1]).build();
		}else if(etcMatcher.find()) {
			return Address.builder()
						  .fullAddress(address)
						  .city(tmp[0])
						  .district(tmp[1])
						  .build();
		}
		
		return Address.builder().fullAddress(address).build();
	}
}
