package com.example.springbootehcache.caching;

import com.example.springbootehcache.events.CachingEvent;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class CacheEventRedirect implements CacheEventListener<Object, Object> {

  @Autowired
  public ApplicationEventPublisher applicationEventPublisher;

  @Override
  public void onEvent(CacheEvent<?, ?> event) {
    applicationEventPublisher.publishEvent(new CachingEvent(event.getType(), event.getKey()));
  }
}
