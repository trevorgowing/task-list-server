package com.trevorgowing.tasklist.test.encoder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

public class JsonEncoder {

  public static String encodeToString(Object object) {
    ObjectWriter objectWriter = Jackson2ObjectMapperBuilder.json().build().writer();
    try {
      return objectWriter.writeValueAsString(object);
    } catch (JsonProcessingException jpe) {
      throw new RuntimeException(jpe);
    }
  }
}
