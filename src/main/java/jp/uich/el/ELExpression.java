package jp.uich.el;

import java.io.Serializable;
import java.util.Map;

import jp.uich.util.StringUtils;
import lombok.Builder;
import lombok.NonNull;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Builder
@Accessors(fluent = true)
public class ELExpression implements Serializable {

  /** EL式 */
  @NonNull
  private String expression;
  /** EL式に渡すパラメータ */
  @Singular
  private Map<String, Object> args;

  @Override
  public String toString() {
    return StringUtils.toString(this);
  }
}
