/*
 * Copyright (c) 2023 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
package com.example.springbootehcache.config;

import org.ehcache.impl.serialization.StringSerializer;
import org.ehcache.spi.serialization.Serializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;

@Configuration
@EnableCaching
public class CacheConfig implements JCacheManagerCustomizer {

  @Override
  public void customize(CacheManager cacheManager) {
    final var ehcache = cacheManager.unwrap(org.ehcache.CacheManager.class);
    // customize if you need....
  }

  @Bean
  @Qualifier("StringSerializer")
  public Serializer<String> stringSerializer() {
    return new StringSerializer();
  }
}
