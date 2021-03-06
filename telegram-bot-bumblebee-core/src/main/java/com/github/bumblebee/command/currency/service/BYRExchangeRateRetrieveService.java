package com.github.bumblebee.command.currency.service;

import com.github.bumblebee.command.currency.dataprovider.nbrb.Currency;
import com.github.bumblebee.command.currency.dataprovider.nbrb.NBRBExRatesParser;
import com.github.bumblebee.command.currency.domain.SupportedCurrency;
import com.github.bumblebee.command.currency.exception.ExchangeRateRetrieveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class BYRExchangeRateRetrieveService {

    private final NBRBExRatesParser parser;

    @Autowired
    public BYRExchangeRateRetrieveService(NBRBExRatesParser parser) {
        this.parser = parser;
    }

    public Double getCurrentExchangeRate(String currencyName) {

        return getExchangeRate(LocalDate.now(), currencyName);
    }

    public Double getExchangeRate(LocalDate date, SupportedCurrency currency) {

        return getExchangeRate(date, currency.name());
    }

    public Double getExchangeRate(LocalDate date, String currency) {
        try {
            List<Currency> exchangeRates = parser.getDailyRates(date);

            for (Currency rate : exchangeRates) {
                if (currency.toUpperCase().equals(rate.getShortName())) {
                    return rate.getRate();
                }
            }
            return null;
        } catch (IOException | SAXException e) {
            throw new ExchangeRateRetrieveException(e);
        }
    }

}
