package com.jts.smallbox.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author jts
 */
@Slf4j
@Component
@Order(99)
public class CustomerApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {
        log.info("==========order 99 [{}] execd [{}]=======",this.getClass().getName(),args);
    }
}
