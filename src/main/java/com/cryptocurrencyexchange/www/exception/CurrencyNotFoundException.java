package com.cryptocurrencyexchange.www.exception;

import lombok.ToString;

@ToString
public class CurrencyNotFoundException extends RuntimeException {

    public CurrencyNotFoundException(final String currencyName) {
        super("Currency name not found: " + currencyName);
    }

    public CurrencyNotFoundException(final String firstCurrencyName, final String secondCurrencyName) {
        super("Currency name not found: " + firstCurrencyName + ", " + secondCurrencyName);
    }
}
