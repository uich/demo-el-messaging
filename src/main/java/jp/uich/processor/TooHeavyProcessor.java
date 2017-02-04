package jp.uich.processor;

import java.util.Date;

import jp.uich.dto.Item;
import jp.uich.dto.User;

public interface TooHeavyProcessor {

  static final String BEAN_NAME = "tooHeavyProcessor";

  void action(User user, Item item, Date date);

  void process(String key, Date date);
}
