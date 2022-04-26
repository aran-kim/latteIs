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

    @GetMapping("/search")
    public String search(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            int init = userRepository.findByUsername(user.getUsername()).getInit();
            if(init == 0) // 관심사 없음
                model.addAttribute("moreInfo", 0);
        }
        return "search";
    }

    @GetMapping("/friend")
    public String friend(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            User userinfo = userRepository.findByUsername(user.getUsername());
            Interest userInterest = interestRepository.findByUserId(userinfo.getId());

            String userMBTI = userInterest.getMbti();
            System.out.println("사용자의 MBTI : " + userMBTI);
            String good;
            if(userMBTI.equals("ENFJ") || userMBTI.equals("INTJ")){
                if(userMBTI.equals("ENFJ"))
                    good = "INTJ";
                else
                    good = "ENFJ";
            }
            else if(userMBTI.equals("ENTJ") || userMBTI.equals("INFJ")){
                if(userMBTI.equals("ENTJ"))
                    good = "INFJ";
                else
                    good = "ENTJ";
            }
            else if(userMBTI.equals("ESTJ") || userMBTI.equals("ISFJ")){
                if(userMBTI.equals("ESTJ"))
                    good = "ISFJ";
                else
                    good = "ESTJ";
            }
            else if(userMBTI.equals("ESFJ") || userMBTI.equals("ISTJ")){
                if(userMBTI.equals("ESFJ"))
                    good = "ISTJ";
                else
                    good = "ESFJ";
            }
            else if(userMBTI.equals("ENFP") || userMBTI.equals("ISFP")){
                if(userMBTI.equals("ENFP"))
                    good = "ISFP";
                else
                    good = "ENFP";
            }
            else if(userMBTI.equals("ENTP") || userMBTI.equals("ISTP")){
                if(userMBTI.equals("ENTP"))
                    good = "ISTP";
                else
                    good = "ENTP";
            }
            else if(userMBTI.equals("ESTP") || userMBTI.equals("INTP")){
                if(userMBTI.equals("ESTP"))
                    good = "INTP";
                else
                    good = "ESTP";
            }
            else if(userMBTI.equals("ESFP") || userMBTI.equals("INFP")){
                if(userMBTI.equals("ESFP"))
                    good = "INFP";
                else
                    good = "ESFP";
            }
            else
                good = null;
            System.out.println("good : " + good);
            //good = "INTJ"; //테스트용

            List<Interest> userList = interestRepository.findAllByMbti(good);
            model.addAttribute("userList", userList);

            System.out.println("userList : " + userList);
        }
        return "friend";
    }

    @GetMapping("/friendDetail")
    public String friendDetail(@RequestParam(value = "user_id") Long user_id, Model model, @LoginUser SessionUser user, Interest interest){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            System.out.println("받은 user_id : " + user_id);
            model.addAttribute("user_id", user_id);
            interest = interestRepository.findByUserId(user_id.intValue());
            model.addAttribute("interest", interest);

        }
        return "friendDetail";
    }

}
