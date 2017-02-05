package jp.uich.util;

import org.apache.commons.lang3.ClassUtils;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

  private static ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json()
    .indentOutput(true)
    .simpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    .timeZone("Asia/Tokyo")
    .build()
    .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

  @SneakyThrows
  public static String toString(Object object) {
    return ClassUtils.getSimpleName(object, "null") + ":" + objectMapper.writeValueAsString(object);
  }

}
