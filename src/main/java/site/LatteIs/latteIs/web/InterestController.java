package site.LatteIs.latteIs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.LatteIs.latteIs.auth.LoginUser;
import site.LatteIs.latteIs.auth.SessionUser;
import site.LatteIs.latteIs.web.domain.entity.Interest;
import site.LatteIs.latteIs.web.domain.entity.MBTI;
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.InterestRepository;
import site.LatteIs.latteIs.web.domain.repository.MBTIRepository;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

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
        Interest interest = interestRepository.findByUserId(userinfo.getId());
        mbti.setUser(userinfo);
        System.out.println("MBTI save 전 정보 : " + mbti);

        int[] num = new int[4];
        num[0] = mbti.getQ1() + mbti.getQ2() + mbti.getQ3();
        num[1] = mbti.getQ4() + mbti.getQ5() + mbti.getQ6();
        num[2] = mbti.getQ7() + mbti.getQ8() + mbti.getQ9();
        num[3] = mbti.getQ10() + mbti.getQ11() + mbti.getQ12();
        String[] str = {"I_E", "S_N", "T_F", "J_P"};
        for(int i = 0; i < 4; i++){
            if(num[i] > 1)
                str[i] = str[i].substring(str[i].lastIndexOf("_") + 1);
            else
                str[i] = str[i].substring(0,1);
        }
        String _mbti = String.join("", str);
        System.out.println("mbti : " + _mbti);
        mbti.setMbti(_mbti);

        mbtiRepository.save(mbti);
        System.out.println("MBTI save 후 정보 : " + mbti);

        return "redirect:/question";
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
    public String questionProc(Model model, @LoginUser SessionUser user, Interest interest,
                               @RequestParam List<String> characteristic, @RequestParam List<String> hobby, @RequestParam List<String> friend_style){

        User user1 = userRepository.findByUsername(user.getUsername());
        interest.setUser(user1);
        MBTI mbti = mbtiRepository.findByUserId(user1.getId());
        interest.setMbti(mbti.getMbti());

        System.out.println("Interest save 전 정보 : " + interest);
        interestRepository.save(interest);
        System.out.println("Interest save 후 정보 : " + interest);

        System.out.println(interest.getBirthday());
        int year = Integer.parseInt(interest.getBirthday().substring(0, 4));
        int age = Calendar.getInstance().get(Calendar.YEAR) - year + 1;
        System.out.println(age);
        interest.setAge(age);

        System.out.println(characteristic.toString() + hobby.toString() + friend_style.toString());
        interest.setCharacteristic(characteristic.toString());
        interest.setHobby(hobby.toString());
        interest.setFriend_style(friend_style.toString());

        user1.setInit(1);

        return "redirect:/";
    }


}
