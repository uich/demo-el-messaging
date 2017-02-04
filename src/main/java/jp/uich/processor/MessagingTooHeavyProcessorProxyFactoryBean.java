package jp.uich.processor;

import java.lang.reflect.Proxy;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import jp.uich.el.ELExpression;
import jp.uich.el.SpelUtils;
import jp.uich.receiver.ELReceiver;
import jp.uich.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Profile("messaging")
@Component(TooHeavyProcessor.BEAN_NAME)
@Slf4j(topic = "送信側")
public class MessagingTooHeavyProcessorProxyFactoryBean implements FactoryBean<TooHeavyProcessor>, InitializingBean {

  private final String beanName = TooHeavyProcessor.BEAN_NAME;
  private final Class<TooHeavyProcessor> type = TooHeavyProcessor.class;

  private TooHeavyProcessor proxy;

  @Autowired
  private AmqpTemplate messageTemplate;

  @Override
  public void afterPropertiesSet() {
    this.proxy = (TooHeavyProcessor) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(),
      new Class[] { this.type }, (bean, method, args) -> {
        ELExpression el = SpelUtils.createELContext(this.beanName, method, args);

        log.info("{}", StringUtils.toString(el));

        this.messageTemplate.convertAndSend(ELReceiver.EXCHANGE_NAME, null, el);
        return null;
      });
  }

  @Override
  public TooHeavyProcessor getObject() throws Exception {
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
