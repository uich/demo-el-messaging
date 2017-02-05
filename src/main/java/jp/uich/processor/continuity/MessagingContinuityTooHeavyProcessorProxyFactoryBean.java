package jp.uich.processor.continuity;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import jp.uich.el.ELExpression;
import jp.uich.el.ELExpressions;
import jp.uich.el.ELRecorder;
import jp.uich.el.SpelUtils;
import jp.uich.receiver.RecordedELReceiver;
import lombok.extern.slf4j.Slf4j;

@Profile("messaging")
@Component(ContinuityTooHeavyProcessor.BEAN_NAME)
@Slf4j(topic = "送信側")
public class MessagingContinuityTooHeavyProcessorProxyFactoryBean
  implements FactoryBean<ContinuityTooHeavyProcessor>, InitializingBean {

  private final String beanName = ContinuityTooHeavyProcessor.BEAN_NAME;
  private final Class<ContinuityTooHeavyProcessor> type = ContinuityTooHeavyProcessor.class;

  private ContinuityTooHeavyProcessor proxy;

  @Autowired
  private AmqpTemplate messageTemplate;

  @Override
  public void afterPropertiesSet() {
    this.proxy = (ContinuityTooHeavyProcessor) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(),
      new Class[] { this.type }, (bean, method, args) -> {
        ELExpression el = SpelUtils.createELExpression(this.beanName, method, args);

        ELRecorder.record(el);

        if (this.isFinish(bean, method, args)) {
          ELExpressions els = ELRecorder.getAllRecords();
          if (els.getContexts().size() == 1) {
            // finishしか呼び出されていなければ実行しない。
            return null;
          }
          log.info("{}", els);
          this.messageTemplate.convertAndSend(RecordedELReceiver.EXCHANGE_NAME, null, els);
        }

        return null;
      });
  }

  protected boolean isFinish(Object bean, Method method, Object[] args) {
    if (method.getName().equals("finish") && ArrayUtils.isEmpty(args)) {
      return true;
    }
    return false;
  }

  @Override
  public ContinuityTooHeavyProcessor getObject() throws Exception {
    return this.proxy;
  }

  @Override
  public Class<?> getObjectType() {
    return this.type;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

}
