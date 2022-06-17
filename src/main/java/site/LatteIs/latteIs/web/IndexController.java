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

import javax.servlet.http.HttpSession;

@Controller // View 반환
public class IndexController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private HttpSession httpSession;

    @GetMapping("/") // "Localhost:8080"
    public String index(Model model, @LoginUser SessionUser user) {

        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            System.out.println("Init : " + user.getInit());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            model.addAttribute("init", user.getInit());
        }
        return "index"; //src/main/resources/templates/index.mustache
    }

    @CrossOrigin("*")
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/logout")
    public String logout(){
        httpSession.setAttribute("user", null);
        return "logout";
    }

    @GetMapping("/join")
    public String check(){
        System.out.println("checkControl 통과");
        return "join/join";
    }

    @ResponseBody
    @GetMapping("/join/sendSMS")
    public String sendSMS(@RequestParam ("phoneNumber") String phoneNumber) throws CoolsmsException {
        System.out.println("sendSMS 통과");
        return PhoneAuthenticationService.phoneAuthentication(phoneNumber);
    }

    @GetMapping("/joinID")
    public String join(){
        return "join/joinID";
    }

    @PostMapping("/joinProc")
    public String joinProc(User user){
        System.out.println("회원가입 진행 전 : " + user);
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword); // 비밀번호 인코딩해서 저장
        user.setRole("ROLE_USER");
        user.setInit(0);
        user.setImage("no_profile.png");
        userRepository.save(user); // 회원가입
        System.out.println("회원가입 진행 후 : " + user);
        return "redirect:/loginForm";
    }

}
