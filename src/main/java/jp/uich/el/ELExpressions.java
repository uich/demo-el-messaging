package jp.uich.el;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.commons.lang3.SerializationUtils;

import lombok.Data;
import lombok.NonNull;

/**
 * 複数の{@link ELContext}を管理するコンポーネント。
 *
 * @author morimichi_yuichi
 */
@Data
public class ELExpressions implements Serializable, Cloneable {

  private static final long serialVersionUID = 1L;

  private final List<ELExpression> contexts = new ArrayList<>();

  /**
   * {@link ELExpression}を追加します。
   * @param context {@link ELExpression}
   */
  public void add(@NonNull ELExpression context) {
    this.contexts.add(context);
  }

  /**
   * 全ての{@link ELExpression}に対して操作可能な{@link Stream}を生成します。
   * @return {@link Stream}
   */
  public Stream<ELExpression> stream() {
    return this.contexts.stream();
  }

  /**
   * 全ての{@link ELExpression}に対して指定のアクションを実行します。
   * @param action {@link Consumer}
   */
  public void forEach(@NonNull Consumer<ELExpression> action) {
    this.contexts.forEach(action);
  }

  //  @Override
  //  public String toString() {
  //    StrBuilder sb = new StrBuilder();
  //    sb.appendln("ELExpressions(");
  //
  //    if (CollectionUtils.isNotEmpty(this.contexts)) {
  //      IntStream.range(0, this.contexts.size())
  //        .forEach(idx -> {
  //          sb.appendln((idx + 1) + ":" + this.contexts.get(idx));
  //        });
  //    }
  //
  //    sb.append(")");
  //
  //    return sb.build();
  //  }

  @Override
  public ELExpressions clone() {
    return SerializationUtils.clone(this);
  }
}
