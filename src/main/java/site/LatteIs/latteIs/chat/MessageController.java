package site.LatteIs.latteIs.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.socket.TextMessage;
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MessageController {

    private final SimpMessageSendingOperations sendingOperations;

    @Autowired
    ChatMessageRepository chatMessageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/send")
    public void sendMsg(ChatDTO message) throws Exception{
        ChatMessage chatMessage = new ChatMessage();
        String sender = message.getUser();
        String type = message.getType();
        ChatRoom chatRoom = chatRoomRepository.findById(Long.parseLong(message.getChatRoom()));

        User user = userRepository.findByNickName(sender);

        List<ChatMessage> list = chatMessageRepository.findAllMessageByRoomIdandUserId(chatRoom.getId(), user.getId());

        if (!(type.equals("ENTER")) || (list.size() == 0)) {
            int currentNumber = chatRoom.getCurrentnumber();
            currentNumber++;
            System.out.println(currentNumber);
            if (type.equals("ENTER")){
                if (currentNumber > chatRoom.getMaxnumber()) {
                    return;
                } else {
                    chatRoom.setCurrentnumber(currentNumber);
                    chatRoomRepository.save(chatRoom);
                }
            }
            chatMessage.setType(type);

            String str = message.getMessage();
            if (str.contains("씨발")) {
                str = str.replace("씨발", "**");
                message.getMessage().replace(message.getMessage(), str);
            }
            chatMessage.setMessage(str);
            chatMessage.setChatRoom(chatRoom);
            chatMessage.setMe(false);
            chatMessage.setUser(user);
            chatMessageRepository.save(chatMessage); // 대화저장
            sendingOperations.convertAndSend("/sub/chat/chatting?id="+message.getChatRoom(),message);
        }
    }
}
