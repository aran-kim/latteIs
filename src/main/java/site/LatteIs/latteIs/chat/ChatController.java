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

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpResponse;
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
        chatroom.setCurrentnumber(0);
        chatRoomRepository.save(chatroom); // 방 만들기
        return "redirect:/mychatroom";
    }


    @GetMapping("/chat")
    public String chat(Model model, @RequestParam(value="id")int room_id, @LoginUser SessionUser user, HttpServletResponse response) throws IOException {
        if(user != null){
            User finduser = userRepository.findByUsername(user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("chatroom",chatRoomRepository.findById(room_id));
            List<ChatMessage> list = chatMessageRepository.findAllMessageByRoomIdandUserId(room_id,finduser.getId());
            List<ChatMessage> list1 = chatMessageRepository.findMessageByRoomId(room_id);
            ChatMessage forceout = chatMessageRepository.findTypeOutByUserId(finduser.getId(),room_id);
            model.addAttribute("roomsize", chatRoomRepository.findById(room_id).getCurrentnumber());
            model.addAttribute("maxroomsize", chatRoomRepository.findById(room_id).getMaxnumber());
            if(forceout != null){
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('강제퇴장 퇴장 되어서 채팅방에 접속 불가능합니다.');location.href = '/mychatroom';</script>");
                out.flush();
            }
            else if(chatRoomRepository.findById(room_id).getCurrentnumber() +1 > chatRoomRepository.findById(room_id).getMaxnumber() && list.size() == 0) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('채팅방 입장 인원을 초과하였습니다.');history.go(-1);</script>");
                out.flush();
            }
            else{
                for(int i=0; i<list.size(); i++){
                    if(list.get(i).getUser().getUsername().equals(user.getUsername())){
                        list.get(i).setMe(true);
                    }else list.get(i).setMe(false);
                }
                for(int i=0; i<list1.size(); i++){
                    if(list1.get(i).getUser().getUsername().equals(user.getUsername())){
                        list1.get(i).setMe(true);
                    }else list1.get(i).setMe(false);
                }
                model.addAttribute("room",chatMessageRepository.findMessageByRoomId(room_id));
                model.addAttribute("saveMessage", chatMessageRepository.findAllMessageByRoomIdandUserId(room_id,finduser.getId()));
            }
        }
        return "chat";
    }

    @GetMapping("/exitchatroom")
    public String exitChatRoom(@RequestParam(value="id")long room_id, @LoginUser SessionUser user){
        User finduser = userRepository.findByUsername(user.getUsername());
        ChatMessage chatMessage = chatMessageRepository.findMessageByRoomIdandUserId(room_id, finduser.getId());
        chatMessageRepository.delete(chatMessage);
        ChatRoom chatRoom = chatRoomRepository.findById(room_id);
        int cnumber = chatRoom.getCurrentnumber();
        chatRoom.setCurrentnumber(cnumber-1);
        chatRoomRepository.save(chatRoom);

        return "redirect:/mychatroom";
    }

    @GetMapping("/forceexitchatroom")
    public String forceexitChatRoom(@RequestParam(value="room_id")long room_id, @RequestParam(value="user_id")int user_id, @LoginUser SessionUser user){
        ChatMessage chatMessage = chatMessageRepository.findMessageByRoomIdandUserId(room_id, user_id);
        chatMessageRepository.delete(chatMessage);
        ChatRoom chatRoom = chatRoomRepository.findById(room_id);
        int cnumber = chatRoom.getCurrentnumber();
        chatRoom.setCurrentnumber(cnumber-1);
        chatRoomRepository.save(chatRoom);
        return "redirect:/chat?id="+room_id;
    }

}