package site.LatteIs.latteIs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.LatteIs.latteIs.auth.LoginUser;
import site.LatteIs.latteIs.auth.SessionUser;
import site.LatteIs.latteIs.web.domain.entity.Interest;
import site.LatteIs.latteIs.web.domain.entity.MBTI;
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.InterestRepository;
import site.LatteIs.latteIs.web.domain.repository.MBTIRepository;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.List;

@Controller
public class InterestController {

    @Autowired
    UserRepository userRepository;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    MBTIRepository mbtiRepository;

    @GetMapping("/mbti")
    public String mbti(Model model, @LoginUser SessionUser user, HttpServletResponse response) throws IOException {
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            return "join/mbti";
        }
        else{
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인부터 하고 오시죠?');location.href='/loginForm';</script>");
            out.flush();
            return "";
        }
    }

    @ResponseBody
    @PostMapping("/mbtiProc")
    public void mbtiProc(Model model, @LoginUser SessionUser user, MBTI _mbti, @RequestParam (value = "mbti", required = false) String mbti){
        User userinfo = userRepository.findByUsername(user.getUsername());
        Interest interest = interestRepository.findByUserId(userinfo.getId());
        System.out.println("mbti : " + mbti);

        _mbti.setMbti(mbti);
        _mbti.setUser(userinfo);
        mbtiRepository.save(_mbti);
        System.out.println("MBTI save 후 정보 : " + _mbti);

        userinfo.setMbti(mbti);
        userinfo.setInit(1);
        userRepository.save(userinfo);
        System.out.println("userinfo : " + userinfo);

    }

    @GetMapping("/question")
    public String question(Model model, @LoginUser SessionUser user, HttpServletResponse response) throws IOException{
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            return "join/question";
        }
        else{
            response.setContentType("text/html; charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("<script>alert('로그인부터 하고 오시죠?');location.href='/loginForm';</script>");
            out.flush();
            return "";
        }

    }

    @PostMapping("/questionProc")
    public String questionProc(Model model, @LoginUser SessionUser user, Interest interest,
                               @RequestParam List<String> characteristic, @RequestParam List<String> hobby, @RequestParam List<String> friend_style){

        User userinfo = userRepository.findByUsername(user.getUsername());
        interest.setUser(userinfo);
        MBTI mbti = mbtiRepository.findByUserId(userinfo.getId());
        interest.setMbti(mbti.getMbti());

        System.out.println("Interest save 전 정보 : " + interest);

        System.out.println(interest.getBirthday());
        int year = Integer.parseInt(interest.getBirthday().substring(0, 4));
        int age = Calendar.getInstance().get(Calendar.YEAR) - year + 1;
        System.out.println(age);
        interest.setAge(age);
        System.out.println(characteristic.toString() + hobby.toString() + friend_style.toString());
        interest.setCharacteristic(characteristic.toString());
        interest.setHobby(hobby.toString());
        interest.setFriend_style(friend_style.toString());

        interestRepository.save(interest);
        System.out.println("Interest save 후 정보 : " + interest);

        userinfo.setInit(2);
        userRepository.save(userinfo);

        return "redirect:/";
    }


}
