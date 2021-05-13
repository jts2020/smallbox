package com.jts.smallbox.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author jts
 */
@Slf4j
@Component
public class ImportantMethodInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        Method method = methodInvocation.getMethod();
        ImportantInterceptor annotation = method.getAnnotation(ImportantInterceptor.class);

        if (log.isInfoEnabled()) {
            log.info("Exec methodInterceptor,method[{}],annotationValue[{}],param{}", method.getName(),
                    annotation.value(), methodInvocation.getArguments());
        }
        return methodInvocation.proceed();
    }
}
