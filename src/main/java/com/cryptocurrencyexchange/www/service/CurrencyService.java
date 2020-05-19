package com.cryptocurrencyexchange.www.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CurrencyService {

    Map<String, BigDecimal> getCurrenciesRates(final String currencyName, final Long filter);

    List<String> exchangeCurrency(final String sourceCurrency, final String outputCurrency, final BigDecimal amount);
}
