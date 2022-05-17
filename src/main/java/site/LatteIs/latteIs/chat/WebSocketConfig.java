package site.LatteIs.latteIs.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
@Configuration
@EnableWebSocket //Websocket 활성화
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        /*Endpoint는 "/chat", 다른 서버에서도 접속이 가능하도록 setAllowedOrigin("*") 추가*/
        registry.addHandler(chatHandler, "/ws/chat/chatting").setAllowedOrigins("*");
    }
}
