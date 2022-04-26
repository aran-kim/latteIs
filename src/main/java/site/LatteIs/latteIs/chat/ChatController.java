package site.LatteIs.latteIs.chat;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.LatteIs.latteIs.auth.LoginUser;
import site.LatteIs.latteIs.auth.SessionUser;
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

import java.util.List;

@Controller // View 반환
@Log4j2
public class ChatController {

    @Autowired
    private ChatRoomRepository chatRoomRepository;
    @Autowired
    private ChatMessageRepository chatMessageRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/mychatroom")
    public String mychatroom(Model model, @LoginUser SessionUser user){
        if(user != null){
            // 쿼리 조인 후 입장 중인 대화방 찾기
            User finduser = userRepository.findByUsername(user.getUsername());
            List<ChatMessage> chatMessage = chatMessageRepository.findMessageByUserId(finduser.getId());

            List list = chatRoomRepository.findByMasterusername(user.getUsername());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("chatroom",list);
            model.addAttribute("mychatroom",chatMessage);
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
            User finduser = userRepository.findByUsername(user.getUsername());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("chatroom",chatRoomRepository.findById(room_id));
            List<ChatMessage> list = chatMessageRepository.findAllMessageByRoomIdandUserId(room_id,finduser.getId());
            for(int i=0; i<list.size(); i++){
                System.out.println("list user "+ list.get(i).getUser().getUsername());
                if(list.get(i).getUser().getUsername().equals(user.getUsername())){
                    list.get(i).setMe(true);
                }else list.get(i).setMe(false);
            }
            model.addAttribute("saveMessage", chatMessageRepository.findAllMessageByRoomIdandUserId(room_id,finduser.getId()));
        }
        return "chat";
    }

    @GetMapping("/exitchatroom")
    public String exitChatRoom(@RequestParam(value="id")long room_id, @LoginUser SessionUser user){
        User finduser = userRepository.findByUsername(user.getUsername());
        ChatMessage chatMessage = chatMessageRepository.findMessageByRoomIdandUserId(room_id, finduser.getId());
        chatMessageRepository.delete(chatMessage);

        return "redirect:/mychatroom";
    }
}
