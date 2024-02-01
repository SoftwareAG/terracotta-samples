package com.example.springbootehcache.caching;

import com.example.springbootehcache.model.Currency;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ehcache.spi.serialization.Serializer;
import org.ehcache.spi.serialization.SerializerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * This is a dumb serializer showing we can inject spring beans into Ehcache implementations
 */
@Component
public class CurrenciesSerializer implements Serializer<List<Currency>> {

  @Autowired
  public ObjectMapper mapper;

  @Autowired
  @Qualifier("StringSerializer")
  public Serializer<String> stringSerializer;

  @Override
  public ByteBuffer serialize(List<Currency> object) throws SerializerException {
    try {
      String json = mapper.writeValueAsString(object);
      return stringSerializer.serialize(json);
    } catch (JsonProcessingException e) {
      throw new SerializerException(e);
    }
  }

  @Override
  public List<Currency> read(ByteBuffer binary) throws ClassNotFoundException, SerializerException {
    try {
      final String json = stringSerializer.read(binary);
      return mapper.readValue(json, new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      throw new SerializerException(e);
    }
  }

  @Override
  public boolean equals(List<Currency> object, ByteBuffer binary) throws ClassNotFoundException, SerializerException {
    try {
      String json = mapper.writeValueAsString(object);
      return stringSerializer.equals(json, binary);
    } catch (JsonProcessingException e) {
      throw new SerializerException(e);
    }
  }
}
