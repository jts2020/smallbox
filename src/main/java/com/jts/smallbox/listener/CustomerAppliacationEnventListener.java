package com.jts.smallbox.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/**
 * @author jts
 */
@Slf4j
@Component
public class CustomerAppliacationEnventListener implements ApplicationListener<ApplicationEvent> {
    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        if (applicationEvent instanceof ApplicationEnvironmentPreparedEvent){
            log.info("env init..");
        } else if (applicationEvent instanceof ApplicationPreparedEvent){
            log.info("init completed..");
        } else if (applicationEvent instanceof ContextRefreshedEvent) {
            log.info("application refreshed..");
        } else if (applicationEvent instanceof ApplicationReadyEvent) {
            log.info("application completed..");
        } else if (applicationEvent instanceof ContextStartedEvent) {
            log.info("application started..");
        } else if (applicationEvent instanceof ContextStoppedEvent) {
            log.info("application stopped..");
        } else if (applicationEvent instanceof ContextClosedEvent) {
            log.info("application closed..");
        }
    }
}
