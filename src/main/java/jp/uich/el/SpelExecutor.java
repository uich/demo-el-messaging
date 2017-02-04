package jp.uich.el;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import lombok.NonNull;

/**
 * {@link ELContextExecutor}のSpEL式実装クラス。
 *
 * @author morimichi_yuichi
 */
@Component
public class SpelExecutor implements ELExecutor {

  private ExpressionParser parser = new SpelExpressionParser();

  @Autowired
  private ApplicationContext appContext;

  @Override
  public Object execute(@NonNull ELExpression el) {
    StandardEvaluationContext context = this.createContext(el.args());
    return this.parser.parseExpression(el.expression()).getValue(context);
  }

  /**
   * SpELのパースルールを生成します。
   * @param args メソッドの引数
   * @return SpELのパースルールコンテキスト
   */
  private StandardEvaluationContext createContext(Map<String, Object> args) {
    StandardEvaluationContext context = new StandardEvaluationContext();

    args.forEach((key, value) -> context.setVariable(key, value));
    context.setBeanResolver((_self, beanName) -> this.appContext.getBean(beanName));

    return context;
  }
}
