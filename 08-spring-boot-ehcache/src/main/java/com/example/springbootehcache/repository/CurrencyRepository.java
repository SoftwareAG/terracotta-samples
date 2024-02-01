/*
 * Copyright (c) 2023 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
package com.example.springbootehcache.repository;

import com.example.springbootehcache.model.Currency;

import java.util.Collection;

public interface CurrencyRepository {
  Collection<Currency> getCurrencies();
  double getLatestRate(String code);
}