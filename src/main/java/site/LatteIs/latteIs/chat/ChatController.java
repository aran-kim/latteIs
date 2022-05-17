package site.LatteIs.latteIs.chat;


import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
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

    @GetMapping("/chat/chatroom")
    public String mychatroomwithparam(Model model, @RequestParam(value = "searchkey", required = false) String searchKey, @LoginUser SessionUser user) {
        if (user != null) {
            // 쿼리 조인 후 입장 중인 대화방 찾기
            User finduser = userRepository.findByUsername(user.getUsername());
            List<ChatMessage> chatMessage = chatMessageRepository.findMessageByUserId(finduser.getId());
            if (searchKey == null) searchKey = "% %";
            else searchKey = "%" + searchKey + "%";
            List<ChatRoom> searchedRoom = chatRoomRepository.findBySearchkey(searchKey);
            System.out.println("search : " + searchedRoom);

            List list = chatRoomRepository.findByMasterusername(user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            model.addAttribute("chatroom", list);
            model.addAttribute("mychatroom", chatMessage);
            model.addAttribute("searchedRoom", searchedRoom);
        }
        return "mychatroom";
    }


    @PostMapping("/chat/createChatRoom")
    public String createChatRoom(ChatRoom chatroom, @LoginUser SessionUser user) {
        chatroom.setRoomname(chatroom.getRoomname());
        chatroom.setMasterusername(user.getNickName());
        chatroom.setMaxnumber(chatroom.getMaxnumber());
        chatroom.setCurrentnumber(0);
        chatroom.setSearch(chatroom.getSearch());
        chatRoomRepository.save(chatroom); // 방 만들기
        return "redirect:/chat/chatroom";
    }

    @PostMapping("/chat/modifyChatroom")
    public String modifyChatroom(ChatRoom chatroom, @LoginUser SessionUser user) {
        chatroom.setRoomname(chatroom.getRoomname());
        long id = chatroom.getId();
        ChatRoom chatRoom = chatRoomRepository.findById(id);
        chatRoom.setMaxnumber(chatroom.getMaxnumber());
        chatRoom.setSearch(chatroom.getSearch());
        chatRoom.setRoomname(chatroom.getRoomname());
        chatRoomRepository.save(chatRoom); // 방 만들기
        return "redirect:/chat/chatting?id=" + id;
    }


    @GetMapping("/chat/chatting")
    public String chat(Model model, @RequestParam(value = "id") int room_id, @LoginUser SessionUser user, HttpServletResponse response) throws IOException {
        if (user != null) {
            User finduser = userRepository.findByUsername(user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("chatroom", chatRoomRepository.findById(room_id));
            List<ChatMessage> list = chatMessageRepository.findAllMessageByRoomIdandUserId(room_id, finduser.getId());
            List<ChatMessage> list1 = chatMessageRepository.findMessageByRoomId(room_id);
            ChatMessage forceout = chatMessageRepository.findTypeOutByUserId(finduser.getId(), room_id);
            model.addAttribute("roomsize", chatRoomRepository.findById(room_id).getCurrentnumber());
            model.addAttribute("maxroomsize", chatRoomRepository.findById(room_id).getMaxnumber());
            boolean master = false;
            if (chatRoomRepository.findById(room_id).getMasterusername().equals(user.getNickName())) {
                master = true;
            }
            model.addAttribute("master", master);
            if (forceout != null) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('강제퇴장 퇴장 되어서 채팅방에 접속 불가능합니다.');location.href = '/chat/chatroom';</script>");
                out.flush();
            } else if (chatRoomRepository.findById(room_id).getCurrentnumber() + 1 > chatRoomRepository.findById(room_id).getMaxnumber() && list.size() == 0) {
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                out.println("<script>alert('채팅방 입장 인원을 초과하였습니다.');history.go(-1);</script>");
                out.flush();
            } else {
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getUser().getUsername().equals(user.getUsername())) {
                        list.get(i).setMe(true);
                    } else list.get(i).setMe(false);
                }
                for (int i = 0; i < list1.size(); i++) {
                    if (list1.get(i).getUser().getUsername().equals(user.getUsername())) {
                        list1.get(i).setMe(true);
                    } else list1.get(i).setMe(false);
                }
                System.out.println(user.getUsername());
                model.addAttribute("room", chatMessageRepository.findMessageByRoomId(room_id));
                model.addAttribute("saveMessage", chatMessageRepository.findAllMessageByRoomIdandUserId(room_id, finduser.getId()));
            }
        }
        return "chat";
    }

    @GetMapping("/chat/exitchatroom")
    public String exitChatRoom(@RequestParam(value = "id") long room_id, @LoginUser SessionUser user) {
        User finduser = userRepository.findByUsername(user.getUsername());
        ChatMessage chatMessage = chatMessageRepository.findMessageByRoomIdandUserId(room_id, finduser.getId());
        chatMessageRepository.delete(chatMessage);
        ChatRoom chatRoom = chatRoomRepository.findById(room_id);
        int cnumber = chatRoom.getCurrentnumber();
        chatRoom.setCurrentnumber(cnumber - 1);
        chatRoomRepository.save(chatRoom);

        return "redirect:/chat/chatroom";
    }

    @GetMapping("/chat/forceexitchatroom")
    public String forceexitChatRoom(@RequestParam(value = "room_id") long room_id, @RequestParam(value = "user_id") int user_id, @LoginUser SessionUser user) {
        ChatMessage chatMessage = chatMessageRepository.findMessageByRoomIdandUserId(room_id, user_id);
        chatMessageRepository.delete(chatMessage);
        ChatRoom chatRoom = chatRoomRepository.findById(room_id);
        int cnumber = chatRoom.getCurrentnumber();
        chatRoom.setCurrentnumber(cnumber - 1);
        chatRoomRepository.save(chatRoom);
        return "redirect:/chat/chatting?id=" + room_id;
    }

    @GetMapping("/chat/changeMaster")
    public String changeMaster(@RequestParam(value = "room_id") long room_id, @RequestParam(value = "user_id") int user_id, @LoginUser SessionUser user) {
        ChatRoom chatRoom = chatRoomRepository.findById(room_id);
        User finduser = userRepository.findById(user_id);
        chatRoom.setMasterusername(finduser.getNickName());
        chatRoomRepository.save(chatRoom);
        return "redirect:/chat/chatting?id=" + room_id;
    }

    @GetMapping("/chat/personalChat")
    public String personalChat(@RequestParam(value = "user_id") int user_id, @LoginUser SessionUser user, HttpServletResponse response) throws IOException {
        User user1 = userRepository.findById(user_id);
        User user2 = userRepository.findByUsername(user.getNickName());
        System.out.println("user1 : " + user1.getNickName());
        System.out.println("user2 : " + user2.getNickName());
        if (user1.getNickName().equals(user2.getNickName())) {
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('같은 유저끼리는 개인톡이 불가능합니다.');history.go(-1);</script>");
            out.flush();
            return "chat";
        } else {
            List<ChatRoom> room = chatRoomRepository.findByMaxnumber(2);
            boolean isexist = false;
            long roomidx = 0;
            for (int i = 0; i < room.size(); i++) {
                int mem = 0;

                List<ChatMessage> userMessage = chatMessageRepository.findMessageByRoomId(room.get(i).getId());
                for (int j = 0; j < userMessage.size(); j++) {
                    if (userMessage.get(j).getUser() == user1 || userMessage.get(j).getUser() == user2) {
                        mem++;
                    }
                }
                if (mem == 2) {
                    isexist = true;
                    roomidx = room.get(i).getId();
                }
            }
            if (isexist == true) {
                return "redirect:/chat/chatting?id=" + roomidx;
            } else {
                ChatRoom chatroom = new ChatRoom();
                chatroom.setRoomname(user1.getNickName() + " 와 " + user2.getNickName() + "의 개인 톡방");
                chatroom.setMasterusername(user1.getNickName() + user2.getNickName());
                chatroom.setMaxnumber(2);
                chatroom.setCurrentnumber(2);
                chatroom.setSearch(0);
                chatRoomRepository.save(chatroom);

                System.out.println(chatroom.getId());

                ChatMessage chatMessage1 = new ChatMessage();
                chatMessage1.setType("ENTER");
                chatMessage1.setMessage("님이 채팅방을 들어왔습니다.");
                chatMessage1.setChatRoom(chatroom);
                chatMessage1.setMe(false);
                chatMessage1.setUser(user1);
                chatMessageRepository.save(chatMessage1); // 대화저장

                ChatMessage chatMessage2 = new ChatMessage();
                chatMessage2.setType("ENTER");
                chatMessage2.setMessage("님이 채팅방을 들어왔습니다.");
                chatMessage2.setChatRoom(chatroom);
                chatMessage2.setMe(false);
                chatMessage2.setUser(user2);
                chatMessageRepository.save(chatMessage2); // 대화저장

                return "redirect:/chat/chatting?id=" + chatroom.getId();
            }
        }
    }
}