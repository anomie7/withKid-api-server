package com.withkid.api.domain;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Getter @Setter
@Entity @ToString
@EqualsAndHashCode
public class Price {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "PRICE_ID")
	private Long id;
	private String name;
	private int price;
	private boolean defaultPrice;
	private String ticketInfo;
	private String extraInfo;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "INTERPARK_ID")
	private InterParkContent interpark;

	public Price(Long id, String name, int price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}
}
