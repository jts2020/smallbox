package com.jts.smallbox.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author jts
 */
@Slf4j
@Component
public class UseTimeHandlerInterceptor implements HandlerInterceptor {

    private static final String WEB_REQ_TIMER_START = "WEB_REQ_TIMER_START";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("Current URI[{}],req_begin..", request.getRequestURI());
        request.setAttribute(WEB_REQ_TIMER_START, System.nanoTime());
        if (handler instanceof HandlerMethod) {
            HandlerMethod controllerMethod = (HandlerMethod) handler;
            Arrays.stream(controllerMethod.getMethod().getAnnotations()).forEach(annotation ->
                    log.info("Method[{}],annotation[{}]", controllerMethod.getMethod().getName(), annotation.annotationType()));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Object timer = request.getAttribute(WEB_REQ_TIMER_START);
        if (log.isInfoEnabled()) {
            log.info("Current URI[{}],useTime[{}]ms,req_end..", request.getRequestURI(), useTime(timer));
        }
    }

    private long useTime(Object timer) {
        if (Objects.nonNull(timer)) {
            return TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - (long) timer);
        }
        return -1L;
    }
}
