/*
 * Copyright (c) 2023 Software AG, Darmstadt, Germany and/or Software AG USA Inc., Reston, VA, USA, and/or its subsidiaries and/or its affiliates and/or their licensors.
 * Use, reproduction, transfer, publication or disclosure is prohibited except as specifically provided for in your License Agreement with Software AG.
 */
package com.example.springbootehcache.config;

import com.example.springbootehcache.events.CachingEvent;
import org.ehcache.config.builders.CacheEventListenerConfigurationBuilder;
import org.ehcache.event.EventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import java.time.Duration;
import java.util.ArrayList;
import java.util.EnumSet;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.ExpiryPolicyBuilder.timeToLiveExpiration;
import static org.ehcache.config.builders.ResourcePoolsBuilder.heap;

@Configuration
@EnableCaching
public class CacheConfig implements JCacheManagerCustomizer {

  @Autowired
  public volatile ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void customize(CacheManager cacheManager) {
    final var ehcache = cacheManager.unwrap(org.ehcache.CacheManager.class);

    final var cacheEventListenerConfiguration = CacheEventListenerConfigurationBuilder
        .newEventListenerConfiguration(event -> applicationEventPublisher.publishEvent(new CachingEvent(event.getType(), event.getKey())), EnumSet.allOf(EventType.class))
        .ordered()
        .asynchronous();

    ehcache.createCache("rates", newCacheConfigurationBuilder(String.class, Double.class, heap(5))
        .withExpiry(timeToLiveExpiration(Duration.ofSeconds(60)))
        .withService(cacheEventListenerConfiguration));

    ehcache.createCache("currencies", newCacheConfigurationBuilder(String.class, ArrayList.class, heap(1))
        .withExpiry(timeToLiveExpiration(Duration.ofSeconds(60)))
        .withService(cacheEventListenerConfiguration));
  }
}
