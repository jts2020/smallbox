package com.jts.smallbox.runner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author jts
 */

@Slf4j
@Component
@Order(98)
public class CustomerCommandLineRunner implements CommandLineRunner {

    @Override
    public void run(String... args) {
        log.info("==========order 98 [{}] execd [{}]=======",this.getClass().getName(),args);
    }
}
