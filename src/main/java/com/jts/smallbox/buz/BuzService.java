package com.jts.smallbox.buz;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jts.smallbox.buz.dao.TblUserDao;
import com.jts.smallbox.buz.entity.TblUser;
import com.jts.smallbox.interceptor.ImportantInterceptor;
import com.jts.smallbox.mq.disruptor.MessageBo;
import com.jts.smallbox.proxy.IUserOp;
import com.jts.smallbox.proxy.User;
import com.jts.smallbox.proxy.enhancer.UserEnhaner;
import com.jts.smallbox.proxy.jdk.UserJdk;
import com.jts.smallbox.websocket.WsService;
import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * @author jts
 */
@Slf4j
@Service
public class BuzService {

    private ConfigurableApplicationContext context;

    private final RingBuffer<MessageBo> messageBoRingBuffer;

    private UserJdk userJdk;

    private UserEnhaner userEnhaner;

    private TblUserDao tblUserDao;

    private RestTemplate restTemplate = new RestTemplate();

    public BuzService(RingBuffer<MessageBo> messageBoRingBuffer) {
        this.messageBoRingBuffer = messageBoRingBuffer;
    }

    @Autowired
    public void setUserJdk(UserJdk userJdk) {
        this.userJdk = userJdk;
    }

    @Autowired
    public void setUserEnhaner(UserEnhaner userEnhaner) {
        this.userEnhaner = userEnhaner;
    }

    @Autowired
    public void setTblUserDao(TblUserDao tblUserDao) {
        this.tblUserDao = tblUserDao;
    }

    @Autowired
    public void setContext(ConfigurableApplicationContext context) {
        this.context = context;
    }

    @ImportantInterceptor("getStr")
    public String buzIndex(String param) {
        log.info("invoked BuzService.buzIndex[{}]", param);
        Integer id = Optional.of(param).map(Integer::parseInt).get();
        Page<TblUser> tblUserPage = new Page<>(2, 3);
        Wrapper<TblUser> wrapper = Wrappers.<TblUser>lambdaQuery()
                .isNotNull(TblUser::getId);
        IPage<TblUser> userPage = tblUserDao.selectPage(tblUserPage, wrapper);
        log.info("Page tblUserList [{}]", userPage.getRecords());
        TblUser tblUerBySql = tblUserDao.getById(id);
        log.info("Sql id [{}],tblUser [{}]", id, tblUerBySql);
        invockUserJdk().getInfo();
        invockUserEnhancer().getInfo();
        StringJoiner res = new StringJoiner("_");
        res.add("buzIndex").add(param).add(tblUerBySql.toString());
        pushDisruptor(param);
        log.info("res BuzService.buzIndex[{}]", res);
        return res.toString();
    }

    public void shutdown(){
        context.close();
        log.info("shutdown success");
    }

    public String wsPush(String param){
        new Thread(()->{
            String url = "http://hq.sinajs.cn/list=sh601398";
            while (!WsService.clients.isEmpty()){

                HttpHeaders headers = new HttpHeaders();
                headers.add("referer","http://finance.sina.com.cn");

                HttpEntity<String> req = new HttpEntity<>(null, headers);
                restTemplate.getMessageConverters().set(1,new StringHttpMessageConverter(StandardCharsets.UTF_8));
                ResponseEntity<String> exchange = restTemplate.postForEntity(url,req,String.class);
                String res = exchange.getBody();
                log.debug("get sina.js res [{}]",res);
                String[] resSplit = res.split(",",-1);
                String msg = resSplit[3];
                log.debug("push res [{}]",msg);
                WsService.clients.forEach((id,session) -> WsService.onSend(session,msg));
                try {
                    TimeUnit.SECONDS.sleep(6L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("wsPush over~");
        },"wsPush").start();
        return "SUCCESS";
    }

    private void pushDisruptor(String param) {
        long seq = messageBoRingBuffer.next();
        try {
            MessageBo messageBo = messageBoRingBuffer.get(seq);
            messageBo.setMsg(param);
        } finally {
            messageBoRingBuffer.publish(seq);
            log.info("Seq[{}] push to disruptor", seq);
        }
    }

    private IUserOp invockUserJdk() {
        userJdk.bind(new User());
        return userJdk.newUser();
    }

    private IUserOp invockUserEnhancer() {
        return userEnhaner.newUser();
    }
}
