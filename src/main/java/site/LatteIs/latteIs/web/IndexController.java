package site.LatteIs.latteIs.web;

import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.LatteIs.latteIs.auth.*;
import site.LatteIs.latteIs.web.domain.*;

@Controller // View 반환
public class IndexController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private PostRepository postRepository;

    @GetMapping("/") // "Localhost:8080"
    public String index(Model model, @LoginUser SessionUser user) {

        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("init", user.getInit());
        }
        return "index"; //src/main/resources/templates/index.mustache
    }

    @GetMapping("/user")
    @ResponseBody
    public String user(){
        return "user";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin(){
        return "admin";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/check")
    public String check(){
        System.out.println("checkControl 통과");
        return "sendSMS";
    }

    @ResponseBody
    @GetMapping("/check/sendSMS")
    public String sendSMS(@RequestParam ("phoneNumber") String phoneNumber) throws CoolsmsException {
        System.out.println("sendSMS 통과");
        return PhoneAuthenticationService.phoneAuthentication(phoneNumber);
    }

    @GetMapping("/join")
    public String join(){
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProc(User user, Model model){
        System.out.println("회원가입 진행 전 : " + user);
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword); // 비밀번호 인코딩해서 저장
        user.setRole("ROLE_USER");
        user.setInit(0);
        userRepository.save(user); // 회원가입
        System.out.println("회원가입 진행 후 : " + user);
        return "redirect:/loginForm";
    }

    @GetMapping("/question")
    public String question(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            model.addAttribute("init", user.getInit());
            model.addAttribute("username", user.getUsername());
        }
        return "question";
    }

    @PostMapping("/questionProc")
    public String questionProc(Model model, @LoginUser SessionUser user, Interest interest){
        User user1 = userRepository.findByUsername(user.getUsername());
        interest.setUser(user1);
        System.out.println("Interest save 전 정보 : " + interest);
        interestRepository.save(interest);
        System.out.println("Interest save 후 정보 : " + interest);
        return "redirect:/";
    }

    @GetMapping("/createNickname")
    public String createNickname(User user){
        return "createNickname";
    }

    @PostMapping("/createNicknameProc")
    public String createNicknameProc(User user){
        return "createNickname";
    }

    @GetMapping("/board")
    public String board(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("board", boardRepository.findAll());
        }
        return "board";
    }

    @GetMapping("/board/{id}")
    public String postList(@PathVariable Long id, Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("받은 board_id : " + id);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("board_id", id);
            int check_id = id.intValue();
            model.addAttribute("post", postRepository.findAllByBoardId(check_id));
        }
        return "post";
    }

    @GetMapping("/board/{id}/post/save")
    public String postSave(@PathVariable Long id, Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("받은 board_id : " + id);
            model.addAttribute("username", user.getUsername());
            model.addAttribute("board_id", id);
            int check_id = id.intValue();
            model.addAttribute("post", postRepository.findAllByBoardId(check_id));
        }
        return "postSave";
    }

    @PostMapping("/postSaveProc/{id}")
    public String postSaveProc(@PathVariable Long id, Model model, @LoginUser SessionUser user, Post post){
        User user1 = userRepository.findByUsername(user.getUsername());
        Board board = boardRepository.findById(id.intValue());
        post.setBoard(board);
        post.setUser(user1);
        System.out.println("Post save 전 정보 : " + post);
        postRepository.save(post);
        System.out.println("Post save 후 정보 : " + post);
        return "redirect:/board/" + id;
    }

    @GetMapping("/chat")
    public String chat(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            model.addAttribute("username", user.getUsername());
        }
        return "chat";
    }
}
