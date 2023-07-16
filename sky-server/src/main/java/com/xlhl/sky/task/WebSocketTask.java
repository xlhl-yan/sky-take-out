package com.xlhl.sky.task;

import com.xlhl.sky.websocket.WebSocketServer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class WebSocketTask {
    @Resource
    private WebSocketServer webSocketServer;

    /**
     * 通过WebSocket每隔5秒向客户端发送消息
     */
//    @Scheduled(cron = "0/5 * * * * ?")
//    public void sendMessageToClient() {
//        webSocketServer.sendToAllClient("这是来自服务端的消息：" +
//                DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now())
//        );
//    }
}
