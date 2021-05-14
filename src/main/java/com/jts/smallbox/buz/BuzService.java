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
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;

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
        WsService.clients.forEach((id,session) -> WsService.onSend(session,Objects.toString(System.nanoTime())));
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
