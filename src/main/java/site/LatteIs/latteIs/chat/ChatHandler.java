package site.LatteIs.latteIs.chat;

import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import site.LatteIs.latteIs.web.domain.User;
import site.LatteIs.latteIs.web.domain.UserRepository;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Log4j2 // 자바 로깅 프레임워크 // xml, json 등 구성 지원
public class ChatHandler extends TextWebSocketHandler {
    private static List<WebSocketSession> list = new ArrayList<WebSocketSession>();
    HashMap<String, WebSocketSession> sessionMap = new HashMap<>(); //웹소켓 세션을 담아둘 맵

    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;

    @Override // 메시지 전송 핸들러
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatMessage chatMessage = new ChatMessage();
        String msg = message.getPayload();
        JSONObject obj = jsonToObjectParser(msg);
        for(String key : sessionMap.keySet()) {
            WebSocketSession wss = sessionMap.get(key);
            try {
                wss.sendMessage(new TextMessage(obj.toJSONString()));

                chatMessage.setType(obj.get("messagetype").toString());
                chatMessage.setMessage(obj.get("msg").toString());
                long room_id = Long.parseLong(obj.get("roomId").toString());
                ChatRoom chatRoom = chatRoomRepository.findById(room_id);
                chatMessage.setChatRoom(chatRoom);
                String user_name = obj.get("sender").toString();
                User user = userRepository.findByUsername(user_name);
                chatMessage.setUser(user);

                System.out.println("save 전 chatMessage : " + chatMessage);
                chatMessageRepository.save(chatMessage); // 대화저장
                System.out.println("save 후 chatMessage : " + chatMessage);
            }catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        sessionMap.put(session.getId(), session);
        JSONObject obj = new JSONObject();
        obj.put("type", "getId");
        obj.put("sender", session.getId());
        session.sendMessage(new TextMessage(obj.toJSONString()));
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //소켓 종료
        sessionMap.remove(session.getId());
        super.afterConnectionClosed(session, status);
    }

    private static JSONObject jsonToObjectParser(String jsonStr) {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;
        try {
            obj = (JSONObject) parser.parse(jsonStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return obj;
    }
}
