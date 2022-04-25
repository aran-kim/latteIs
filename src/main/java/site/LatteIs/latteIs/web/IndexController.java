package site.LatteIs.latteIs.web;

import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.LatteIs.latteIs.auth.*;
import site.LatteIs.latteIs.web.domain.entity.*;
import site.LatteIs.latteIs.web.domain.repository.*;

import java.util.List;

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
    @Autowired
    private MBTIRepository mbtiRepository;


    @GetMapping("/") // "Localhost:8080"
    public String index(Model model, @LoginUser SessionUser user) {

        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
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
    public String joinProc(User user){
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
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
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

    @GetMapping("/board")
    public String board(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            model.addAttribute("board", boardRepository.findAll());
        }
        return "board";
    }

    @GetMapping("/post")
    public String post(@RequestParam(value = "board_id") Long board_id, Model model, @LoginUser SessionUser user){
        if(user != null){

            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            System.out.println("받은 board_id : " + board_id);
            model.addAttribute("board_id", board_id);

            int check_id = board_id.intValue();
            //List<Post> post = postRepository.findPostNickNameByBoardId(check_id);
            List<Post> post = postRepository.findAllByBoardId(check_id);
            System.out.println("findPostNickName Test : " + post);
            model.addAttribute("post", post);

        }
        return "post";
    }

    @GetMapping("/postSave")
    public String postSave(@RequestParam(value = "board_id") Long board_id, Model model, @LoginUser SessionUser user){
        if(user != null){

            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            System.out.println("받은 board_id : " + board_id);
            model.addAttribute("board_id", board_id);
            int check_id = board_id.intValue();
            model.addAttribute("post", postRepository.findAllByBoardId(check_id));
        }
        return "postSave";
    }

    @PostMapping("/postSaveProc")
    public String postSaveProc(@RequestParam(value = "board_id") Long board_id, Model model, @LoginUser SessionUser user, Post post){
        User user1 = userRepository.findByUsername(user.getUsername());
        Board board = boardRepository.findById(board_id.intValue());
        post.setBoard(board);
        post.setUser(user1);
        System.out.println("Post save 전 정보 : " + post);
        postRepository.save(post);
        System.out.println("Post save 후 정보 : " + post);
        return "redirect:/post?board_id=" + board_id;
    }

    @GetMapping("/postDetail")
    public String postDetail(@RequestParam(value = "board_id") Long board_id, @RequestParam(value = "post_id") Long post_id, Model model, @LoginUser SessionUser user, Post post){
        if(user != null){

            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            System.out.println("받은 board_id : " + board_id);
            System.out.println("받은 post_id : " + post_id);
            model.addAttribute("board_id", board_id);
            post = postRepository.findById(post_id.intValue());
            model.addAttribute("post", post);
            int writerId = post.getUser().getId();
            model.addAttribute("writername", userRepository.findById(writerId).getUsername());
        }
        return "postDetail";
    }

    @GetMapping("/info")
    public String info(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            User userinfo = userRepository.findByUsername(user.getUsername());
            model.addAttribute("userinfo", userinfo);
        }
        return "info";
    }

    @GetMapping("/changePwd")
    public String changePwd(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
        }
        return "changePwd";
    }

    @PostMapping("/changePwdProc")
    public String changePwdProc(Model model, @LoginUser SessionUser user, @RequestParam ("NewPassword") String NewPassword, @RequestParam ("NewPasswordCheck") String NewPasswordCheck){
        if(NewPassword.equals(NewPasswordCheck)){
            User userinfo = userRepository.findByUsername(user.getUsername());
            System.out.println("변경 전 user 정보 : " + userinfo);
            String encPassword = bCryptPasswordEncoder.encode(NewPassword);
            userinfo.setPassword(encPassword);
            System.out.println("변경 후 user 정보 : " + userinfo);
            return "redirect:/info";
        }
        else{
            return "redirect:/changePwd";
        }
    }

    @GetMapping("/changeEmail")
    public String changeEmail(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            model.addAttribute("email", user.getEmail());
        }
        return "changeEmail";
    }

    @PostMapping("/changeEmailProc")
    public String changeEmailProc(Model model, @LoginUser SessionUser user, @RequestParam ("email") String email, @RequestParam ("password") String password){
        User userinfo = userRepository.findByUsername(user.getUsername());
        if(password.equals(userinfo.getPassword())){
            System.out.println("변경 전 user 정보 : " + userinfo);
            userinfo.setEmail(email);
            System.out.println("변경 후 user 정보 : " + userinfo);
            return "redirect:/info";
        }
        else{
            return "redirect:/changeEmail";
        }
    }

    @GetMapping("/changeNickName")
    public String changeNickName(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
        }
        return "changeNickName";
    }

    @PostMapping("/changeNickNameProc")
    public String changeNickNameProc(Model model, @LoginUser SessionUser user, @RequestParam ("nickname") String nickname){
        User userinfo = userRepository.findByUsername(user.getUsername());
        System.out.println("변경 전 user 정보 : " + userinfo);
        userinfo.setNickName(nickname);
        System.out.println("변경 후 user 정보 : " + userinfo);
        return "redirect:/info";
    }

    @GetMapping("/mbti")
    public String mbti(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
        }
        return "mbti";
    }

    @PostMapping("/mbtiProc")
    public String mbtiProc(Model model, @LoginUser SessionUser user, MBTI mbti){
        User userinfo = userRepository.findByUsername(user.getUsername());
        mbti.setUser(userinfo);
        System.out.println("MBTI save 전 정보 : " + mbti);
        mbtiRepository.save(mbti);
        //계산하는 서비스 클래스 넣기
        System.out.println("MBTI save 후 정보 : " + mbti);
        return "redirect:/";
    }


    @GetMapping("/search")
    public String search(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
        }
        return "search";
    }

    @GetMapping("/search/friend")
    public String friend(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            User userinfo = userRepository.findByUsername(user.getUsername());
            Interest userInterest = interestRepository.findByUserId(userinfo.getId());
            String userMBTI = userInterest.getMbti();

        }
        return "friend";
    }

}
