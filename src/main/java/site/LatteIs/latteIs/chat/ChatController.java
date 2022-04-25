package site.LatteIs.latteIs.chat;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.LatteIs.latteIs.auth.LoginUser;
import site.LatteIs.latteIs.auth.SessionUser;

import java.util.List;

@Controller // View 반환
@Log4j2
public class ChatController {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @GetMapping("/mychatroom")
    public String mychatroom(Model model, @LoginUser SessionUser user){
        if(user != null){
            List list = chatRoomRepository.findByMasterusername(user.getUsername());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("chatroom",list);
        }
        return "mychatroom";
    }

    @PostMapping("/createChatRoom")
    public String createChatRoom(ChatRoom chatroom, @LoginUser SessionUser user){

        chatroom.setRoomname(chatroom.getRoomname());
        chatroom.setMasterusername(user.getUsername());

        chatRoomRepository.save(chatroom); // 방 만들기
        return "redirect:/mychatroom";
    }

    @GetMapping("/chat")
    public String chat(Model model,@RequestParam(value="id")int room_id, @LoginUser SessionUser user){
        if(user != null){
            model.addAttribute("username", user.getUsername());
            model.addAttribute("chatroom",chatRoomRepository.findById(room_id));
            model.addAttribute("saveMessage", chatMessageRepository.findAllMessageByRoomId(room_id));
        }
        return "chat";
    }
}
