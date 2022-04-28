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
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
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
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        ChatMessage chatMessage = new ChatMessage();
        String msg = message.getPayload();
        JSONObject obj = jsonToObjectParser(msg);
        for(String key : sessionMap.keySet()) {
            WebSocketSession wss = sessionMap.get(key);
            try {
                long room_id = Long.parseLong(obj.get("roomId").toString());
                ChatRoom chatRoom = chatRoomRepository.findById(room_id);
                String user_name = obj.get("sender").toString();
                User user = userRepository.findByUsername(user_name);
                List<ChatMessage> list = chatMessageRepository.findAllMessageByRoomIdandUserId(chatRoom.getId(),user.getId());

                System.out.println("list size: " + list.size());
                System.out.println("type: " + obj.get("messagetype").toString());

                if(!(obj.get("messagetype").toString().equals("ENTER")) || (list.size()==0)) {
                    int currentNumber = chatRoom.getCurrentnumber();
                    currentNumber++;
                    if(obj.get("messagetype").toString().equals("ENTER")) {
                        System.out.println("romm memeber: " + currentNumber);
                        if(currentNumber > chatRoom.getMaxnumber()){
/*                          MyHttpServletResponse response = new MyHttpServletResponse();
                            response.setContentType("text/html; charset=UTF-8");
                            PrintWriter out = response.getWriter();
                            out.println("<script>alert('채팅방 입장 인원을 초과하였습니다.');</script>");
                            out.flush();*/
                            break;
                        }else{
                            chatRoom.setCurrentnumber(currentNumber);
                        }
                    } // 아직 미완성 - 경고창이 안뜸

                    chatMessage.setType(obj.get("messagetype").toString());

                    String str = obj.get("msg").toString();
                    if(str.contains("씨발")){
                        str = str.replace("씨발","**");
                        obj.replace("msg",str);
                    }
                    chatMessage.setMessage(str);
                    chatMessage.setChatRoom(chatRoom);

                    chatMessage.setUser(user);

                    wss.sendMessage(new TextMessage(obj.toJSONString()));

                    System.out.println("save 전 chatMessage : " + chatMessage);
                    chatMessageRepository.save(chatMessage); // 대화저장
                    System.out.println("save 후 chatMessage : " + chatMessage);
                }

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
