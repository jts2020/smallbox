package com.jts.smallbox.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@ServerEndpoint(value = "/ws/test")
public class WsService {

    public static Map<String, Session> clients = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session){
        log.debug("onOpen Sid[{}]",session.getId());
        clients.put(session.getId(),session);
    }

    @OnClose
    public void onClose(Session session){
        log.debug("onClose Sid[{}]",session.getId());
        clients.remove(session.getId());
    }

    @OnMessage
    public void onMsg(String msg,Session session){
        log.debug("onMsg Sid[{}]",session.getId());
        onSend(session,"HHH"+msg);
    }

    @OnError
    public void onErr(Session session,Throwable error){
        log.error("onErr Sid[{}]",session.getId(),error);
        try {
            session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            clients.remove(session.getId());
        }
    }

    public static void onSend(Session toSession,String msg){
        try {
            log.debug("onSend Sid[{}]",toSession.getId());
            toSession.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
