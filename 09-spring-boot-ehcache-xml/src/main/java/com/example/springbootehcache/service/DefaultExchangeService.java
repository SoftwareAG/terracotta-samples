/*
 * Copyright (c) 2023 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
package com.example.springbootehcache.service;

import com.example.springbootehcache.events.MethodCallEvent;
import com.example.springbootehcache.model.Currency;
import com.example.springbootehcache.repository.CurrencyRepository;
import io.micrometer.observation.annotation.Observed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

@Component
public class DefaultExchangeService implements ExchangeService {

  @Autowired
  public CurrencyRepository currencyRepository;

  @Autowired
  public ApplicationEventPublisher applicationEventPublisher;

  @Override
  @Observed(name = "ExchangeService#getCodes")
  public Collection<String> getCodes() {
    applicationEventPublisher.publishEvent(new MethodCallEvent(getClass(), "getCodes"));
    return currencyRepository.getCurrencies().stream().map(Currency::code).sorted().collect(toList());
  }

  @Override
  @Observed(name = "ExchangeService#convert")
  public double convert(double fromAmount, String fromCurrency, String toCurrency) {
    requireNonNull(fromCurrency);
    requireNonNull(toCurrency);
    applicationEventPublisher.publishEvent(new MethodCallEvent(getClass(), "convert"));
    final var usdToFrom = currencyRepository.getLatestRate(fromCurrency);
    final var usdToTo = currencyRepository.getLatestRate(toCurrency);
    return fromAmount / usdToFrom * usdToTo;
  }
}
