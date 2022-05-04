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
    public String mychatroomwithparam(Model model,@RequestParam(value="searchkey", required = false)String searchKey, @LoginUser SessionUser user){
        if(user != null){
            // 쿼리 조인 후 입장 중인 대화방 찾기
            User finduser = userRepository.findByUsername(user.getUsername());
            List<ChatMessage> chatMessage = chatMessageRepository.findMessageByUserId(finduser.getId());
            if(searchKey == null) searchKey = "% %";
            else searchKey = "%" + searchKey + "%";
            List<ChatRoom> searchedRoom = chatRoomRepository.findBySearchkey(searchKey);
            System.out.println("search : "+searchedRoom);

            List list = chatRoomRepository.findByMasterusername(user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            model.addAttribute("chatroom",list);
            model.addAttribute("mychatroom",chatMessage);
            model.addAttribute("searchedRoom",searchedRoom);
        }
        return "mychatroom";
    }



    @PostMapping("/createChatRoom")
    public String createChatRoom(ChatRoom chatroom, @LoginUser SessionUser user){

        chatroom.setRoomname(chatroom.getRoomname());
        chatroom.setMasterusername(user.getNickName());
        chatroom.setMaxnumber(chatroom.getMaxnumber());
        chatroom.setCurrentnumber(1);
        chatRoomRepository.save(chatroom); // 방 만들기
        return "redirect:/mychatroom";
    }


    @GetMapping("/chat")
    public String chat(Model model,@RequestParam(value="id")int room_id, @LoginUser SessionUser user){
        if(user != null){
            User finduser = userRepository.findByUsername(user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("chatroom",chatRoomRepository.findById(room_id));
            List<ChatMessage> list = chatMessageRepository.findAllMessageByRoomIdandUserId(room_id,finduser.getId());
            List<ChatMessage> list1 = chatMessageRepository.findMessageByRoomId(room_id);
            model.addAttribute("roomsize", chatMessageRepository.findMessageByRoomId(room_id).size());

            for(int i=0; i<list.size(); i++){
                if(list.get(i).getUser().getUsername().equals(user.getUsername())){
                    list.get(i).setMe(true);
                }else list.get(i).setMe(false);
                System.out.println("list user "+ list.get(i));
            }
            for(int i=0; i<list1.size(); i++){
                if(list1.get(i).getUser().getUsername().equals(user.getUsername())){
                    list1.get(i).setMe(true);
                }else list1.get(i).setMe(false);
                System.out.println("Room user "+ list1.get(i));
            }
            model.addAttribute("room",chatMessageRepository.findMessageByRoomId(room_id));

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

    @GetMapping("/forceexitchatroom")
    public String forceexitChatRoom(@RequestParam(value="room_id")long room_id, @RequestParam(value="user_id")int user_id, @LoginUser SessionUser user){
        ChatMessage chatMessage = chatMessageRepository.findMessageByRoomIdandUserId(room_id, user_id);
        chatMessageRepository.delete(chatMessage);

        return "redirect:/chat?id="+room_id;
    }

}