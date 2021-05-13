package com.jts.smallbox.mq.disruptor;

import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author jts
 */
@Slf4j
@Component
public class MessageEventHandler implements EventHandler<MessageBo> {
    @Override
    public void onEvent(MessageBo messageBo, long l, boolean b) {
        if(messageBo != null ){
            log.info("Handler seq[{}],msg[{}]",l,messageBo);
        }
    }
}
