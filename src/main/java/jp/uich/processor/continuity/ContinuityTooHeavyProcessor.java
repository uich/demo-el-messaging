package jp.uich.processor.continuity;

import java.util.Date;

import jp.uich.dto.Item;
import jp.uich.dto.User;

public interface ContinuityTooHeavyProcessor {

  static final String BEAN_NAME = "continuityTooHeavyProcessor";

  void action(User user, Item item, Date date);

  void process(String key, Date date);

  void finish();
}
