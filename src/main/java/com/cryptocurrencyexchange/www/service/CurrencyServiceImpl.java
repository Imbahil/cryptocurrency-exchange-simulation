package com.cryptocurrencyexchange.www.service;

import com.cryptocurrencyexchange.www.exception.CurrencyNotFoundException;
import com.cryptocurrencyexchange.www.service.dto.Currencies;
import com.cryptocurrencyexchange.www.service.dto.CurrencyDataDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.math.RoundingMode.HALF_UP;
import static java.util.Arrays.asList;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    public Map<String, BigDecimal> getCurrenciesRates(final String currencyName, final Long filter) {

        return calculateCurrenciesRates(currencyName.toLowerCase().trim(), filter);
    }

    public List<String> exchangeCurrency(final String sourceCurrency, final String outputCurrency, final BigDecimal amount) {
        validateMultipleCurrencyName(sourceCurrency, outputCurrency);

        final String parsedSource = sourceCurrency.trim().toLowerCase();
        final String parsedOutput = outputCurrency.trim().toLowerCase();
        final BigDecimal sourceCurrencyInUSD = getCurrenciesValuesUSD().get(parsedSource);
        final BigDecimal multipliedSourceCurrencyByAmount = sourceCurrencyInUSD.multiply(amount);
        final BigDecimal outputCurrencyInUSD = getCurrenciesValuesUSD().get(parsedOutput);
        final BigDecimal exchangedAmount = multipliedSourceCurrencyByAmount.divide(outputCurrencyInUSD, HALF_UP);
        final BigDecimal fee = multipliedSourceCurrencyByAmount.multiply(new BigDecimal("0.01")).setScale(2, HALF_UP);

        final List<String> result = new ArrayList<>();
        result.add("From currency: " + parsedSource);
        result.add("1 " + parsedSource + " rounded price in USD: " + sourceCurrencyInUSD.setScale(2, HALF_UP).toString());
        result.add("Amount: " + amount.toString());
        result.add("To currency: " + parsedOutput);
        result.add("1 " + parsedOutput + " rounded price in USD: " + outputCurrencyInUSD.setScale(2, HALF_UP).toString());
        result.add("Number of " + outputCurrency + " after simulated exchange: " + exchangedAmount.toString());
        result.add("Fee in USD: " + fee.toString());

        return result;
    }

    private Map<String, BigDecimal> calculateCurrenciesRates(final String currencyName, final Long filter) {
        final HashMap<String, BigDecimal> currenciesWithRates = new HashMap<>();

        validateCurrencyName(currencyName);

        final BigDecimal sourcePriceInUSD = getCurrenciesValuesUSD().get(currencyName);

        getCurrenciesValuesUSD()
                .forEach((k, v) -> currenciesWithRates.put(k, sourcePriceInUSD.divide(v, HALF_UP)));
        currenciesWithRates.remove(currencyName);

        if (filter != null) {
            HashMap ct = (HashMap) currenciesWithRates
                    .entrySet()
                    .stream()
                    .limit(filter)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            currenciesWithRates.clear();
            currenciesWithRates.putAll(ct);
        }

        return currenciesWithRates;
    }

    private Map<String, BigDecimal> getCurrenciesValuesUSD() {
        final List<CurrencyDataDto> assetsList = asList(getAssets().getData());
        final Map<String, BigDecimal> currenciesUSD = new HashMap<>();

        assetsList.forEach(element -> {
            BigDecimal roundedUSDRate = new BigDecimal(element.getPriceUsd()).setScale(10, HALF_UP);
            currenciesUSD.put(element.getId(), roundedUSDRate);
        });
        return currenciesUSD;
    }

    private Currencies getAssets() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject("https://api.coincap.io/v2/assets", Currencies.class);
    }

    private void validateMultipleCurrencyName(String sourceCurrency, String outputCurrency) {
        if (!getCurrenciesValuesUSD().containsKey(sourceCurrency) && !getCurrenciesValuesUSD().containsKey(outputCurrency)) {
            throw new CurrencyNotFoundException(sourceCurrency, outputCurrency);
        } else if (!getCurrenciesValuesUSD().containsKey(sourceCurrency)) {
            throw new CurrencyNotFoundException(sourceCurrency);
        } else validateCurrencyName(outputCurrency);
    }

    private void validateCurrencyName(String currencyName) {
        if (!getCurrenciesValuesUSD().containsKey(currencyName)) {
            throw new CurrencyNotFoundException(currencyName);
        }
    }

}


