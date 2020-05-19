package com.cryptocurrencyexchange.www.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CurrencyDataDto {

    private String id;
    private Long rank;
    private String symbol;
    private String name;
    private Double supply;
    private Double maxSupply;
    private Double marketCapUsd;
    private Double volumeUsd24Hr;
    private Double priceUsd;
    private Double changePercent24Hr;
    private Double vwap24Hr;
}
