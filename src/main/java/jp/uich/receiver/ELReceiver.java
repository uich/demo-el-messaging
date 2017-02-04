package jp.uich.receiver;

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import jp.uich.el.ELExecutor;
import jp.uich.el.ELExpression;
import lombok.extern.slf4j.Slf4j;

@Profile("consumer")
@Component
@Slf4j(topic = "受信側")
public class ELReceiver {

  public static final String QUEUE_NAME = "el.queue";
  public static final String EXCHANGE_NAME = "el.exchange";

  @Autowired
  private ELExecutor elExecutor;

  @RabbitListener(bindings = @QueueBinding(
    value = @Queue(value = QUEUE_NAME, durable = "true"),
    exchange = @Exchange(value = EXCHANGE_NAME, type = ExchangeTypes.FANOUT, durable = "true")))
  public void receive(ELExpression el) {
    log.info("{}", el);

    this.elExecutor.execute(el);
  }

}
