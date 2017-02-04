package jp.uich.processor;

import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jp.uich.dto.Item;
import jp.uich.dto.User;
import jp.uich.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Profile("default")
@Component(TooHeavyProcessor.BEAN_NAME)
@Slf4j(topic = "結果")
public class DefaultTooHeavyProcessor implements TooHeavyProcessor {

  private static final FastDateFormat FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd HH:MM:ss.SSS");

  @Override
  public void action(User user, Item item, Date date) {
    log.info("{} actions to {} at {}!!", StringUtils.toString(user), StringUtils.toString(item),
      FORMATTER.format(date));
  }

  @Override
  public void process(String key, Date date) {
    log.info("process for {} at {}", key, FORMATTER.format(date));
  }

}
