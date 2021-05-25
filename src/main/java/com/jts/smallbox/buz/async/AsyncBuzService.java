package com.jts.smallbox.buz.async;

import com.jts.smallbox.buz.BuzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.AsyncContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
                response.setContentType("application/json");
                ServletOutputStream out = response.getOutputStream();
                for (String param : req.getParameterMap().get("param")) {
                    String res = buzService.buzIndex(param);
                    out.write(res.getBytes(StandardCharsets.UTF_8));
                }
                out.flush();
            } catch (Exception e) {
                log.error("AsyncBuzService.exec URL [{}], param [{}]",uri,req.getParameterMap(),e);
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                asyncContext.complete();
            }
        });
    }


}
