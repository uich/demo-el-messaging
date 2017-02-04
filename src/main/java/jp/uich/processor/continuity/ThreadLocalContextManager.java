package jp.uich.processor.continuity;

import java.util.Date;

import jp.uich.dto.Item;
import jp.uich.dto.User;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

public abstract class ThreadLocalContextManager {

  private static final ThreadLocal<Context> contexts = new ThreadLocal<Context>() {
    @Override
    protected Context initialValue() {
      return new Context();
    }
  };

  public static Context getContext() {
    return contexts.get();
  }

  public static void clear() {
    contexts.remove();
  }

  @Data
  @Setter
  @Accessors(fluent = true)
  public static class Context {
    private User user;
    private Item item;
    private Date date;
    private String key;
  }
}
