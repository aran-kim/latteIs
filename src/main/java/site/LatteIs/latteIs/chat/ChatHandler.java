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
    private static List<HashMap<String, Object>> sessionList = new ArrayList<>();

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
        String rN = obj.get("roomId").toString();
        HashMap<String, Object> temp = new HashMap<String, Object>();

        if (sessionList.size() > 0) {
            for (int i = 0; i < sessionList.size(); i++) {
                String roomNumber =  sessionList.get(i).get("roomNumber").toString();
                if (roomNumber.equals(rN)) {
                    temp = sessionList.get(i);
                    break;
                }
            }
            for (String key : temp.keySet()) {
                if(key.equals("roomNumber")) {
                    continue;
                }
                WebSocketSession wss = (WebSocketSession) temp.get(key);
                try {
                    if(wss == null) continue;
                    long room_id = Long.parseLong(obj.get("roomId").toString());
                    ChatRoom chatRoom = chatRoomRepository.findById(room_id);
                    String user_name = obj.get("sender").toString();
                    User user = userRepository.findByNickName(user_name);

                    List<ChatMessage> list = chatMessageRepository.findAllMessageByRoomIdandUserId(chatRoom.getId(), user.getId());

                    System.out.println("list size: " + list.size());
                    System.out.println("type: " + obj.get("messagetype").toString());

                    if (!(obj.get("messagetype").toString().equals("ENTER")) || (list.size() == 0)) {
                        int currentNumber = chatRoom.getCurrentnumber();
                        currentNumber++;
                        if (obj.get("messagetype").toString().equals("ENTER")) {
                            if (currentNumber > chatRoom.getMaxnumber()) {
                                break;
                            } else {
                                chatRoom.setCurrentnumber(currentNumber);
                                chatRoomRepository.save(chatRoom);
                            }
                        }
                        chatMessage.setType(obj.get("messagetype").toString());

                        String str = obj.get("msg").toString();
                        if (str.contains("씨발")) {
                            str = str.replace("씨발", "**");
                            obj.replace("msg", str);
                        }
                        chatMessage.setMessage(str);
                        chatMessage.setChatRoom(chatRoom);
                        chatMessage.setMe(false);
                        chatMessage.setUser(user);
                        wss.sendMessage(new TextMessage(obj.toJSONString()));
                        chatMessageRepository.save(chatMessage); // 대화저장
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);
        boolean flag = false;
        String url = session.getUri().toString();
        System.out.println(url.split("id=")[1]);
        String roomNumber = url.split("id=")[1];
        int size = sessionList.size();
        int idx = size;
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                String rN = (String) sessionList.get(i).get("roomNumber");
                if (rN.equals(roomNumber)) {
                    flag = true;
                    idx = i;
                    break;
                }
            }
        }
        if (flag) {
            HashMap<String, Object> map = sessionList.get(idx);
            map.put(session.getId(), session);
        } else {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("roomNumber", roomNumber);
            map.put(session.getId(), session);
            sessionList.add(map);
        }
        JSONObject obj = new JSONObject();
        obj.put("type", "getId");
        obj.put("sessionId", session.getId());
        session.sendMessage(new TextMessage(obj.toJSONString()));
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //소켓 종료
        if (sessionList.size() > 0) {
            for (int i = 0; i < sessionList.size(); i++) {
                sessionList.get(i).remove(session.getId());
            }
        }
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
