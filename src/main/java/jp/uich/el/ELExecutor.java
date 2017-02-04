package jp.uich.el;

public interface ELExecutor {

  /**
   * {@link ELExpression}を解析して実行します。
   * @param el {@link ELExpression}
   * @return 実行結果
   */
  Object execute(ELExpression el);

}
