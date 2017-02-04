package jp.uich.processor.continuity;

import java.util.Date;

import org.apache.commons.lang3.time.FastDateFormat;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jp.uich.dto.Item;
import jp.uich.dto.User;
import jp.uich.processor.continuity.ThreadLocalContextManager.Context;
import jp.uich.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Profile("default")
@Component(ContinuityTooHeavyProcessor.BEAN_NAME)
@Slf4j(topic = "結果")
public class DefaultContinuityTooHeavyProcessor implements ContinuityTooHeavyProcessor {

  private static final FastDateFormat FORMATTER = FastDateFormat.getInstance("yyyy-MM-dd HH:MM:ss.SSS");

  @Override
  public void action(User user, Item item, Date date) {
    ThreadLocalContextManager.getContext()
      .user(user)
      .item(item)
      .date(date);
  }

  @Override
  public void process(String key, Date date) {
    ThreadLocalContextManager.getContext()
      .key(key);
  }

  @Override
  public void finish() {
    Context context = ThreadLocalContextManager.getContext();
    log.info("{} get {} and {} at {}",
      StringUtils.toString(context.user()), StringUtils.toString(context.key()), StringUtils.toString(context.item()),
      FORMATTER.format(context.date()));
  }

}
