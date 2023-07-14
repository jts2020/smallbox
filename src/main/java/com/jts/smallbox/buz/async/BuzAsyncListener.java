package com.jts.smallbox.buz.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.AsyncContext;
import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class BuzAsyncListener implements AsyncListener {
    @Override
    public void onComplete(AsyncEvent asyncEvent) throws IOException {

    }

    @Override
    public void onTimeout(AsyncEvent asyncEvent) throws IOException {
        handleException(asyncEvent,"onTimeout",HttpServletResponse.SC_REQUEST_TIMEOUT);
    }

    @Override
    public void onError(AsyncEvent asyncEvent) throws IOException {
        handleException(asyncEvent,"onError",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    public void onStartAsync(AsyncEvent asyncEvent) throws IOException {

    }

    public void handleException(AsyncEvent asyncEvent,String msg,int status){
        AsyncContext asyncContext= asyncEvent.getAsyncContext();
        try{
            ServletRequest req= asyncContext.getRequest();
            String uri= (String) req.getAttribute(Constant.URI);
            log.error("async req [{}]. uri :[{}]",msg,uri);
        }catch (Exception e){
            log.error("handleException catch",e);
        }finally {
            try{
                HttpServletResponse response= (HttpServletResponse) asyncContext.getResponse();
                response.setStatus(status);
            }catch (Exception e1){
                log.error("handleException finally",e1);
            }
            asyncContext.complete();
        }
    }
}
