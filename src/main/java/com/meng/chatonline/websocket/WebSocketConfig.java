package com.meng.chatonline.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @Author xindemeng
 *
 * WebSocket配置处理器
 */
//指明该类为Spring 配置类
@Configuration
//声明该类支持WebSocket
@EnableWebSocket
public class WebSocketConfig extends WebMvcConfigurerAdapter implements WebSocketConfigurer
{
    @Resource
    private MyWebSocketHandler webSocketHandler;

    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry)
    {
        webSocketHandlerRegistry.addHandler(webSocketHandler, "/ws").addInterceptors(new HandShake());
        //SockJS 是一个浏览器上运行的 JavaScript 库，如果浏览器不支持 WebSocket，
        //该库可以模拟对 WebSocket 的支持，实现浏览器和 Web 服务器之间低延迟、全双工、跨域的通讯通道。
        webSocketHandlerRegistry.addHandler(webSocketHandler, "/ws/sockjs")
                .addInterceptors(new HandShake()).withSockJS();
    }
}
