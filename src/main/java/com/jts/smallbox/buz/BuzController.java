package com.jts.smallbox.buz;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author jts
 */
@Slf4j
@RestController
@RequestMapping("/buz")
public class BuzController {

    private final BuzService buzService;

    public BuzController(BuzService busiService) {
        this.buzService = busiService;
    }

    @GetMapping(path = "/index")
    public String busiIndex(String param){
        log.info("invocked BusiController.busiIndex[{}]",param);
        return buzService.busiIndex(param);
    }

    @GetMapping("/shutdown")
    public void shutdown(){
        buzService.shutdown();
    }

    @GetMapping(path = "/ws/push")
    public String wsPush(String param){
        log.info("invocked BusiController.wsPush[{}]",param);
        return buzService.wsPush(param);
    }
}
