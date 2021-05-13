package com.jts.smallbox.mq.disruptor;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ThreadFactory;

/**
 * @author jts
 */
@Slf4j
@Configuration
public class DisruptorConf {

    private final MessageEventFactory messageEventFactory;
    private final MessageEventHandler messageEventHandler;

    public DisruptorConf(MessageEventFactory messageEventFactory, MessageEventHandler messageEventHandler) {
        this.messageEventFactory = messageEventFactory;
        this.messageEventHandler = messageEventHandler;
    }

    @Bean
    public RingBuffer<MessageBo> messageBoRingBuffer(){
        log.info("disruptor conf init..");
        ThreadFactory threadFactory = new CustomizableThreadFactory("disruptor");
        //2的N次方
        int bufferSize = 1024*256;
        Disruptor<MessageBo> dsruptor = new Disruptor<>(messageEventFactory,bufferSize,threadFactory,
                ProducerType.SINGLE,new BlockingWaitStrategy());
        dsruptor.handleEventsWith(messageEventHandler);
        dsruptor.start();
        log.info("disruptor conf finished..");
        return  dsruptor.getRingBuffer();
    }
}
