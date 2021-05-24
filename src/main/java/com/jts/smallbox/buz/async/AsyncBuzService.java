package com.jts.smallbox.buz.async;

import com.jts.smallbox.buz.BuzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.AsyncContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class AsyncBuzService {

    private Executor executor = Executors.newFixedThreadPool(5);

    private BuzAsyncListener buzAsyncListener;
    @Autowired
    public void setBuzAsyncListener(BuzAsyncListener buzAsyncListener) {
        this.buzAsyncListener = buzAsyncListener;
    }

    private BuzService buzService;
    @Autowired
    public void setBuzService(BuzService buzService) {
        this.buzService = buzService;
    }

    public void exec(HttpServletRequest req){
        AsyncContext asyncContext = req.startAsync();
        HttpServletRequest request = (HttpServletRequest)asyncContext.getRequest();
        String uri = req.getRequestURI();
        request.setAttribute(Constant.URI,uri);
        asyncContext.setTimeout(30*1000);
        asyncContext.addListener(buzAsyncListener);
        executor.execute(() -> {
            log.info("URL [{}], param [{}]",uri,req.getParameterMap());
            HttpServletResponse response = (HttpServletResponse)asyncContext.getResponse();
            try {
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                response.setContentType("text/html");
                response.getOutputStream().write(buzService.buzIndex(req.getParameterMap().get("param")[0]).getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                log.info("AsyncBuzService.exec URL [{}], param [{}]",uri,req.getParameterMap(),e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                asyncContext.complete();
            }
        });
    }


}
