package com.cryptocurrencyexchange.www.service.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Currencies {
    private CurrencyDataDto[] data;
    private Long timestamp;
}
