package com.jts.smallbox.interceptor.config;

import com.jts.smallbox.interceptor.ImportantInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

/**
 * @author jts
 */
@Slf4j
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private HandlerInterceptor useTimeHandlerInterceptor;

    private MethodInterceptor importantMethodInterceptor;

    @Resource
    public void setImportantMethodInterceptor(MethodInterceptor importantMethodInterceptor) {
        this.importantMethodInterceptor = importantMethodInterceptor;
    }

    @Resource
    public void setUseTimeHandlerInterceptor(HandlerInterceptor useTimeHandlerInterceptor) {
        this.useTimeHandlerInterceptor = useTimeHandlerInterceptor;
    }

    @PostConstruct
    public void init() {
        log.info("Exec init method");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(useTimeHandlerInterceptor);
        log.info("Add interceptor[{}]", useTimeHandlerInterceptor.getClass().getName());
    }

    @Bean
    DefaultPointcutAdvisor importantPointcutAdvisor() {
        log.info("Add DefaultPointcutAdvisor[{}]", importantMethodInterceptor.getClass().getName());
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor();
        AnnotationMatchingPointcut pointcut = new AnnotationMatchingPointcut(null, ImportantInterceptor.class);
        advisor.setPointcut(pointcut);
        advisor.setAdvice(importantMethodInterceptor);
        return advisor;
    }

    @PreDestroy
    public void preDestroy(){
        log.info("Exec preDestroy method");
    }
}
