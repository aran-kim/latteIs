package site.LatteIs.latteIs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.LatteIs.latteIs.auth.LoginUser;
import site.LatteIs.latteIs.auth.SessionUser;
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

@Controller
public class InfoController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

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
    public String changePwdProc(Model model, @LoginUser SessionUser user, @RequestParam("NewPassword") String NewPassword, @RequestParam ("NewPasswordCheck") String NewPasswordCheck){
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
}
