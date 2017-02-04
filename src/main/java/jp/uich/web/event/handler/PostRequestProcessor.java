package jp.uich.web.event.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;

import jp.uich.el.ELRecorder;
import jp.uich.processor.continuity.ContinuityTooHeavyProcessor;
import jp.uich.processor.continuity.ThreadLocalContextManager;

@Profile("messaging")
@Component
public class PostRequestProcessor implements ApplicationListener<RequestHandledEvent> {

  @Autowired
  private ContinuityTooHeavyProcessor processor;

  @Override
  public void onApplicationEvent(RequestHandledEvent event) {
    try {
      this.processor.finish();
    } finally {
      this.doIgnoreError(ELRecorder::clear);
      this.doIgnoreError(ThreadLocalContextManager::clear);
    }
  }

  private void doIgnoreError(Runnable clearTask) {
    try {
      clearTask.run();
    } catch (Throwable t) {}
  }
}
