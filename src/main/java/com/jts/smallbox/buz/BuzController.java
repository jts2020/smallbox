package com.jts.smallbox.buz;

import com.jts.smallbox.buz.async.AsyncBuzService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author jts
 */
@Slf4j
@RestController
@RequestMapping("/buz")
public class BuzController {

    private final BuzService buzService;

    private final AsyncBuzService asyncBuzService;

    public BuzController(BuzService busiService,AsyncBuzService asyncBuzService) {
        this.buzService = busiService;
        this.asyncBuzService = asyncBuzService;
    }

    @GetMapping(path = "/index")
    public String buzIndex(String param){
        log.info("invoked BuzController.buzIndex[{}]",param);
        return buzService.buzIndex(param);
    }

    @GetMapping("/shutdown")
    public void shutdown(){
        buzService.shutdown();
    }

    @GetMapping(path = "/ws/push")
    public String wsPush(String param){
        log.info("invoked BuzController.wsPush[{}]",param);
        return buzService.wsPush(param);
    }

    @GetMapping("/async")
    public void asyncReq(String param,HttpServletRequest req){
        log.info("invoked BuzController.asyncReq[{}]",param);
        asyncBuzService.exec(req);
        log.info("invoked BuzController.asyncReq over~");
    }
}
