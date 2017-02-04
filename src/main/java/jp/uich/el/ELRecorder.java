package jp.uich.el;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ELRecorder {

  private static final ThreadLocal<ELExpressions> elThreadLocal = new ThreadLocal<ELExpressions>() {
    @Override
    protected ELExpressions initialValue() {
      return new ELExpressions();
    }
  };

  /**
   * {@link ELExpression}を記録します。
   * @param el 記録する{@link ELExpression}
   */
  public static void record(@NonNull ELExpression el) {
    elThreadLocal.get().add(el);
  }

  /**
   * 記録した全ての{@link ELExpression}を含む{@link ELExpressions}を取得します。
   * @return 本スレッド上で記録した全ての{@link ELExpression}
   */
  public static ELExpressions getAllRecords() {
    return elThreadLocal.get().clone();
  }

  /**
   * 記録した{@link ELExpression}レコードを全て削除します。
   */
  public static void clear() {
    if (log.isTraceEnabled()) {
      elThreadLocal.get().forEach(el -> log.trace("clear:[{}]", el));
    }
    elThreadLocal.remove();
    log.trace("cleared");
  }
}
