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
import site.LatteIs.latteIs.web.domain.entity.Blacklist;
import site.LatteIs.latteIs.web.domain.entity.Follower;
import site.LatteIs.latteIs.web.domain.entity.Interest;
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.BlacklistRepository;
import site.LatteIs.latteIs.web.domain.repository.FollowerRepository;
import site.LatteIs.latteIs.web.domain.repository.InterestRepository;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class InfoController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    InterestRepository interestRepository;
    @Autowired
    private HttpSession httpSession;
    @Autowired
    private FollowerRepository followerRepository;
    @Autowired
    private BlacklistRepository blacklistRepository;


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
            System.out.println("변경 전 Session email : " + user.getEmail());
            userinfo.setEmail(email);
            user.setEmail(email);
            httpSession.setAttribute("user", user);
            System.out.println("변경 후 user 정보 : " + userinfo);
            System.out.println("변경 후 Session email : " + user.getEmail());
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
        System.out.println("변경 전 Seesion nickname : " + user.getNickName());
        userinfo.setNickName(nickname);
        user.setNickName(nickname);
        httpSession.setAttribute("user", user);
        System.out.println("변경 후 user 정보 : " + userinfo);
        System.out.println("변경 후 Seesion nickname : " + user.getNickName());
        return "redirect:/info";
    }

    @GetMapping("/followerList")
    public String followerList(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            User userinfo = userRepository.findByUsername(user.getUsername());
            Follower follower =  followerRepository.findByUserId(userinfo.getId());
            System.out.println("로그인 유저 팔로워 상태 : " + follower);
            System.out.println("follwerList : " + follower.getFollowerUserIdList());

            List<Interest> interestList = new ArrayList<Interest>();
            Interest interest = new Interest();
            int end = 0, uId = 0, start = 0;
            boolean loop = true;
            while (loop){
                end = follower.getFollowerUserIdList().indexOf(", ", end);
                System.out.println("end : " + end + ", _e : " + start);
                if(end < 0){
                    uId = Integer.parseInt(follower.getFollowerUserIdList().substring(start, follower.getFollowerUserIdList().length()));
                    loop = false;
                }
                else
                    uId = Integer.parseInt(follower.getFollowerUserIdList().substring(start, end));
                System.out.println("u : " + uId);
                interest = interestRepository.findByUserId(uId);
                System.out.println(interest);
                interestList.add(interest);
                end += 2;
                start = end;
            }
            System.out.println(interestList);
            model.addAttribute("followerList", interestList);

        }
        return "followerList";
    }

    @GetMapping("/blackList")
    public String blackList(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            User userinfo = userRepository.findByUsername(user.getUsername());
            Blacklist blacklist =  blacklistRepository.findByUserId(userinfo.getId());
            System.out.println("로그인 유저 블랙 리스트 상태 : " + blacklist);
            System.out.println("blackList : " + blacklist.getBlackUserIdList());

            List<Interest> interestList = new ArrayList<Interest>();
            Interest interest = new Interest();
            int end = 0, uId = 0, start = 0;
            boolean loop = true;
            while (loop){
                end = blacklist.getBlackUserIdList().indexOf(", ", end);
                System.out.println("end : " + end + ", _e : " + start);
                if(end < 0){
                    uId = Integer.parseInt(blacklist.getBlackUserIdList().substring(start, blacklist.getBlackUserIdList().length()));
                    loop = false;
                }
                else
                    uId = Integer.parseInt(blacklist.getBlackUserIdList().substring(start, end));
                System.out.println("u : " + uId);
                interest = interestRepository.findByUserId(uId);
                System.out.println(interest);
                interestList.add(interest);
                end += 2;
                start = end;
            }
            System.out.println(interestList);
            model.addAttribute("blackList", interestList);
        }
        return "blackList";
    }
}
