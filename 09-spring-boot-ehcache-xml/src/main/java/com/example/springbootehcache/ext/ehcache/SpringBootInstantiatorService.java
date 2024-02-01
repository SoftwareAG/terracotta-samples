package com.example.springbootehcache.ext.ehcache;

import org.ehcache.core.spi.service.InstantiatorService;
import org.ehcache.impl.internal.classes.DefaultInstantiatorService;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceProvider;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Component;

@Component
public class SpringBootInstantiatorService implements InstantiatorService, BeanFactoryAware {
  private final InstantiatorService delegate;
  private BeanFactory beanFactory;

  public SpringBootInstantiatorService() {
    this(new DefaultInstantiatorService());
  }

  public SpringBootInstantiatorService(InstantiatorService delegate) {
    this.delegate = delegate;
  }

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @Override
  public <T> T instantiate(Class<T> clazz, Object... arguments) throws IllegalArgumentException {
    try {
      return beanFactory.getBean(clazz, arguments);
    } catch (NoSuchBeanDefinitionException e) {
      return delegate.instantiate(clazz, arguments);
    }
  }

  @Override
  public void start(ServiceProvider<Service> serviceProvider) {
  }

  @Override
  public void stop() {
  }
}
