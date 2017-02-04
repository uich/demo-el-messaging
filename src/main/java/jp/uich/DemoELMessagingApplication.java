package jp.uich;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class DemoELMessagingApplication {

  public static void main(String[] args) {
    log.info("active profile:" + System.getProperty("spring.profiles.active"));

    SpringApplication.run(DemoELMessagingApplication.class, args);
  }
}
