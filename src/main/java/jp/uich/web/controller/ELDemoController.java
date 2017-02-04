package jp.uich.web.controller;

import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.uich.dto.Item;
import jp.uich.dto.Item.Brand;
import jp.uich.dto.Item.Category;
import jp.uich.dto.User;
import jp.uich.dto.User.BloodType;
import jp.uich.processor.TooHeavyProcessor;
import lombok.Value;

@Profile("producer")
@RestController
public class ELDemoController {

  private Random random = new Random();

  @Autowired
  private TooHeavyProcessor processor;

  @GetMapping("/action")
  public ActionResult action() {
    User user = this.randomUser();
    Item item = this.randomItem();
    Date date = this.randomDate();

    this.processor.action(user, item, this.randomDate());

    return new ActionResult(user, item, date);
  }

  @GetMapping("/process")
  public ProcessResult process() {
    String key = RandomStringUtils.randomAlphanumeric(10);
    Date date = this.randomDate();

    this.processor.process(key, date);

    return new ProcessResult(key, date);
  }

  @Value
  static class ActionResult {
    User user;
    Item item;
    Date date;
  }

  @Value
  static class ProcessResult {
    String key;
    Date date;
  }

  private User randomUser() {
    return User.builder()
      .name(this.extractRandom("goku", "kuririn", "piccolo", "vegeta"))
      .birthdate(this.randomDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
      .bloodType(this.extractRandom(BloodType.values()))
      .build();
  }

  private Item randomItem() {
    return Item.builder()
      .id(this.random.nextLong())
      .sku(RandomStringUtils.randomAlphanumeric(10))
      .name(RandomStringUtils.randomAlphanumeric(10))
      .price(this.random.nextInt())
      .brand(Brand.builder()
        .id(this.random.nextInt())
        .name(this.extractRandom(
          "アディダス",
          "プーマ"))
        .build())
      .category1(Category.builder()
        .id(this.random.nextInt())
        .name(this.extractRandom(
          "アウター",
          "その他"))
        .build())
      .category2(Category.builder()
        .id(this.random.nextInt())
        .name(this.extractRandom(
          "ジャージ",
          "靴"))
        .build())
      .build();
  }

  private <T> T extractRandom(@SuppressWarnings("unchecked") T... values) {
    List<T> list = new ArrayList<>(Arrays.asList(values));
    Collections.shuffle(list);
    return list.get(0);
  }

  private Date randomDate() {
    long randomValue = this.random.nextLong();
    if (randomValue < 0) {
      randomValue = randomValue * -1;
    }
    return DateUtils.setYears(new Date(randomValue), Year.now().getValue());
  }
}
