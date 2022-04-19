package site.LatteIs.latteIs.web;

import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import site.LatteIs.latteIs.auth.*;
import site.LatteIs.latteIs.web.domain.Interest;
import site.LatteIs.latteIs.web.domain.InterestRepository;
import site.LatteIs.latteIs.web.domain.User;
import site.LatteIs.latteIs.web.domain.UserRepository;

import java.sql.Timestamp;

@Controller // View 반환
public class IndexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private InterestRepository interestRepository;

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



    @GetMapping("/chat")
    public String chat(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            model.addAttribute("username", user.getUsername());
        }
        return "chat";
    }
}
