package jp.uich.el;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.MethodParameter;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import lombok.experimental.UtilityClass;

/**
 * SpELを扱う{@link ELExpression}に関するユーティリティを提供するクラス。
 *
 * @author morimichi_yuichi
 */
@UtilityClass
public class SpelUtils {

  private static final DefaultParameterNameDiscoverer PARAM_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

  /**
   * 指定の条件でSpEL式を生成します。
   * @param beanNameForExecuting SpEL上で実行するクラスのBean名
   * @param method SpEL上で実行するメソッド名
   * @param args SpEL上で実行するメソッドに渡す引数
   * @return {@link ELExpression}
   */
  public static ELExpression createELContext(String beanNameForExecuting, Method method, Object[] args) {
    if (ArrayUtils.isEmpty(args)) {
      // 引数が無ければシンプルに構成する
      return ELExpression.builder()
        .expression("@" + beanNameForExecuting + "." + method.getName() + "()")
        .build();
    }

    List<MethodParameter> methodParams = IntStream.range(0, args.length)
      .mapToObj(n -> {
        MethodParameter methodParam = MethodParameter.forMethodOrConstructor(method, n);
        methodParam.initParameterNameDiscovery(PARAM_NAME_DISCOVERER);
        return methodParam;
      })
      .collect(toList());

    // nullを扱う関係上Collectors.toMap()が使用できないため別の方法で name:value 形式のMapを生成する記述する

    // パラメータの単純な name:value 形式
    Map<String, Object> paramNameValues = new LinkedHashMap<>();
    methodParams.forEach(methodParam -> {
      paramNameValues.put(methodParam.getParameterName(), args[methodParam.getParameterIndex()]);
    });

    // 引数にあたるSpEL式表現を生成する
    // {"foo":"bar", "hoge":"fuga"} -> #foo, #hoge
    String argsExpression = paramNameValues.keySet().stream()
      .map(paramName -> "#" + paramName)
      .collect(joining(", "));

    // EL式を生成
    return ELExpression.builder()
      .expression("@" + beanNameForExecuting + "." + method.getName() + "(" + argsExpression + ")")
      .args(Collections.unmodifiableMap(paramNameValues))
      .build();
  }

}
