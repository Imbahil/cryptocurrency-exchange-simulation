package com.cryptocurrencyexchange.www.controller;

import com.cryptocurrencyexchange.www.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class CurrencyController {

    private CurrencyService currencyService;

    @Autowired
    public CurrencyController(final CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @GetMapping(value = {"/currencies/{currency}", "/currencies/{currency}/{filter}"})
    public Map<String, BigDecimal> getCurrencyMarkings(@PathVariable final String currency,
                                                       @PathVariable(required = false) final Long filter) {
        return currencyService.getCurrenciesRates(currency, filter);
    }

    @GetMapping(value = "/currencies/exchange/{fromCurrency}/{toCurrency}/{amount}")
    public List<String> exchangeCurrency(@PathVariable final String fromCurrency,
                                         @PathVariable final String toCurrency,
                                         @PathVariable final BigDecimal amount) {

        return currencyService.exchangeCurrency(fromCurrency, toCurrency, amount);
    }
}
