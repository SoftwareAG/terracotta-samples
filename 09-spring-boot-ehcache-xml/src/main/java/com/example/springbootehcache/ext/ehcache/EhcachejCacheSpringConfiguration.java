package com.example.springbootehcache.ext.ehcache;

import org.ehcache.core.spi.service.InstantiatorService;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Ehcache Spring Boot support which works together with {@link org.springframework.boot.autoconfigure.cache.JCacheCacheConfiguration}.
 * <p>
 * This class will provide a {@link CacheManager} to the latter, and will also use the same mechanism as the latter to customize the properties and the created cache manager.
 */
@AutoConfiguration
@ConditionalOnClass({org.ehcache.CacheManager.class})
@ConditionalOnMissingBean(value = org.springframework.cache.CacheManager.class, name = "cacheResolver")
@EnableConfigurationProperties(CacheProperties.class)
@ConditionalOnProperty("spring.cache.jcache.config")
public class EhcachejCacheSpringConfiguration implements BeanClassLoaderAware {
  private ClassLoader beanClassLoader;

  @Override
  public void setBeanClassLoader(ClassLoader classLoader) {
    this.beanClassLoader = classLoader;
  }

  @Bean
  @ConditionalOnMissingBean
  public InstantiatorService instantiatorService() {
    return new SpringBootInstantiatorService();
  }

  @Bean
  public CacheManager jCacheCacheManager(CacheProperties cacheProperties,
                                         ObjectProvider<javax.cache.configuration.Configuration<?, ?>> defaultCacheConfiguration,
                                         ObjectProvider<JCacheManagerCustomizer> cacheManagerCustomizers,
                                         InstantiatorService instantiatorService) throws IOException {
    CacheManager jCacheCacheManager = createCacheManager(cacheProperties, instantiatorService);
    List<String> cacheNames = cacheProperties.getCacheNames();
    if (!CollectionUtils.isEmpty(cacheNames)) {
      for (String cacheName : cacheNames) {
        jCacheCacheManager.createCache(cacheName, defaultCacheConfiguration.getIfAvailable(MutableConfiguration::new));
      }
    }
    cacheManagerCustomizers.orderedStream().forEach((customizer) -> customizer.customize(jCacheCacheManager));
    return jCacheCacheManager;
  }

  private CacheManager createCacheManager(CacheProperties cacheProperties, InstantiatorService instantiatorService) throws IOException {
    CachingProvider cachingProvider = getCachingProvider(cacheProperties.getJcache().getProvider());
    Properties properties = createCacheManagerProperties(instantiatorService);
    Resource configLocation = cacheProperties.resolveConfigLocation(cacheProperties.getJcache().getConfig());
    if (configLocation != null) {
      return cachingProvider.getCacheManager(configLocation.getURI(), this.beanClassLoader, properties);
    } else {
      return cachingProvider.getCacheManager(null, this.beanClassLoader, properties);
    }
  }

  private Properties createCacheManagerProperties(InstantiatorService instantiatorService) {
    Properties properties = new Properties();
    properties.put("ehcache.service.instantiatorService", instantiatorService);
    return properties;
  }

  private static CachingProvider getCachingProvider(String cachingProviderFqn) {
    if (StringUtils.hasText(cachingProviderFqn)) {
      return Caching.getCachingProvider(cachingProviderFqn);
    }
    return Caching.getCachingProvider();
  }
}
