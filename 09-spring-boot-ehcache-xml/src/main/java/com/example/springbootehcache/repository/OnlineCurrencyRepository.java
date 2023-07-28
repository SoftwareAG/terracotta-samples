/*
 * Copyright (c) 2023 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
package com.example.springbootehcache.repository;

import com.example.springbootehcache.events.MethodCallEvent;
import com.example.springbootehcache.model.Currency;
import io.micrometer.observation.ObservationRegistry;
import io.micrometer.observation.annotation.Observed;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Component
public class OnlineCurrencyRepository implements CurrencyRepository {
  private static final Logger LOGGER = LoggerFactory.getLogger(OnlineCurrencyRepository.class);

  @Value("${app.freecurrencyapi.key}")
  public String apiKey;

  @Autowired
  public ObservationRegistry observationRegistry;

  @Autowired
  public ApplicationEventPublisher applicationEventPublisher;

  @PostConstruct
  public void init() {
    if (apiKey == null || apiKey.isBlank()) {
      throw new IllegalStateException("You must get an API key at https://freecurrencyapi.com/ set: 'app.freecurrencyapi.key=<your-api-key>'");
    }
  }

  @Override
  @Observed(name = "OnlineCurrencyRepository#getCurrencies")
  @Cacheable(cacheNames = "currencies", key = "'currencies'")
  public Collection<Currency> getCurrencies() {
    simulateLatency();
    final var rest = new RestTemplate();
    final var response = rest.getForObject("https://api.freecurrencyapi.com/v1/currencies?apikey=" + apiKey, CurrencyResponse.class);
    final var data = new ArrayList<>(response.data.values());
    LOGGER.info("getCurrencies(): {}", data);
    applicationEventPublisher.publishEvent(new MethodCallEvent(getClass(), "getCurrencies"));
    return data;
  }

  @Override
  @Observed(name = "OnlineCurrencyRepository#getLatestRates")
  @Cacheable(cacheNames = "rates", key = "#code")
  public double getLatestRate(String code) {
    simulateLatency();
    final var rest = new RestTemplate();
    final var response = rest.getForObject("https://api.freecurrencyapi.com/v1/latest?currencies=" + code + "&apikey=" + apiKey, LatestRatesResponse.class);
    final var data = response.data.get(code);
    LOGGER.info("getLatestRate({}): {}", code, data);
    applicationEventPublisher.publishEvent(new MethodCallEvent(getClass(), "getLatestRates"));
    return data;
  }

  private static void simulateLatency() {
    try {
      System.out.println("<LATENCY SIMULATION OF 1s>");
      Thread.sleep(2_000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  record LatestRatesResponse(Map<String, Double> data) {}

  record CurrencyResponse(Map<String, Currency> data) {}
}
