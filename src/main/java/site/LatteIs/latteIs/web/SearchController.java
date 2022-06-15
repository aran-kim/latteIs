package site.LatteIs.latteIs.web;

import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.LatteIs.latteIs.auth.LoginUser;
import site.LatteIs.latteIs.auth.PhoneAuthenticationService;
import site.LatteIs.latteIs.auth.SessionUser;
import site.LatteIs.latteIs.web.domain.entity.Blacklist;
import site.LatteIs.latteIs.web.domain.entity.Follower;
import site.LatteIs.latteIs.web.domain.entity.Interest;
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.BlacklistRepository;
import site.LatteIs.latteIs.web.domain.repository.FollowerRepository;
import site.LatteIs.latteIs.web.domain.repository.InterestRepository;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private FollowerRepository followerRepository;
    @Autowired
    private BlacklistRepository blacklistRepository;

    @GetMapping("/search")
    public String search(Model model, @LoginUser SessionUser user, HttpServletResponse response) throws IOException {
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            int init = userRepository.findByUsername(user.getUsername()).getInit();
            if(init != 2){ // 질문을 다 안함
                response.setContentType("text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                if(init == 0)
                    out.println("<script>alert('당신 MBTI 질문을 안했네요?');location.href='/mbti';</script>");
                if(init == 1)
                    out.println("<script>alert('당신 관심사 질문을 안했네요?');location.href='/question';</script>");
                out.flush();
            }
        }
        return "search/search";
    }

    @GetMapping("/search/mbtiFriend")
    public String mbtiFriend(Model model, @LoginUser SessionUser user){
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
            model.addAttribute("searchMbti", good);

            List<Interest> userList = interestRepository.findAllByMbtiandUniversity(good, userInterest.getUniversity(), userInterest.getUser().getId());
            model.addAttribute("userList", userList);

            System.out.println("userList : " + userList);
        }
        return "search/mbtiFriend";
    }

    @GetMapping("/search/hobbyFriend")
    public String hobbyFriend(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            User userinfo = userRepository.findByUsername(user.getUsername());
            Interest userInterest = interestRepository.findByUserId(userinfo.getId());

            String hobby = userInterest.getHobby();
            System.out.println(hobby);
            String[] arr = {"", "", ""};
            String[] regdx = {",", "\\[", "\\]"};
            if(hobby.contains(",")){
                System.out.println(hobby.split(", ").length + "개");
                int start = 0, end = 0;
                for(int i = 0; i < hobby.split(", ").length; i++){
                    end = hobby.indexOf(",", start);
                    if(end == -1)
                        end = hobby.length();
                    arr[i] = hobby.substring(start, end);
                    for(int j = 0; j < regdx.length; j++)
                        arr[i] = arr[i].replaceAll(regdx[j], "");
                    arr[i] = "%" + arr[i] + "%";
                    start = end + 2;
                    System.out.println("처리 후");
                    System.out.println("arr["+ i + "]: " + arr[i]);
                }
            }
            else{
                System.out.println("1개");
                arr[0] = hobby;
                for(int j = 0; j < regdx.length; j++)
                    arr[0] = arr[0].replaceAll(regdx[j], "");
                arr[0] = "%" + arr[0] + "%";
                System.out.println("arr[0]: " + arr[0]);
            }

            List<Interest> userList = interestRepository.findAllByEqualInterest(arr[0], arr[1], arr[2], userInterest.getUniversity(), userinfo.getId());
            System.out.println("userList : " + userList);

            model.addAttribute("userList", userList);

        }
        return "search/hobbyFriend";
    }

    @GetMapping("/search/anyFriend")
    public String anyFriend(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            User userinfo = userRepository.findByUsername(user.getUsername());
            Interest userInterest = interestRepository.findByUserId(userinfo.getId());

            List<Interest> userList = interestRepository.findAll();
            System.out.println("userList : " + userList);

            model.addAttribute("userList", userList);

        }
        return "search/anyFriend";
    }

    @GetMapping("/search/universityFriend")
    public String universityFriend(Model model, @LoginUser SessionUser user){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            User userinfo = userRepository.findByUsername(user.getUsername());
            Interest userInterest = interestRepository.findByUserId(userinfo.getId());
            model.addAttribute("university", userInterest.getUniversity());

            List<Interest> userList = interestRepository.findAllByUniversity(userInterest.getUniversity());
            System.out.println("userList : " + userList);

            model.addAttribute("userList", userList);

        }
        return "search/universityFriend";
    }

    @GetMapping("/search/friendDetail")
    public String friendDetail(@RequestParam(value = "user_id") Long user_id, Model model, @LoginUser SessionUser user, Interest friendInterest){
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());
            User userinfo = userRepository.findByUsername(user.getUsername());

            System.out.println("받은 user_id : " + user_id);
            model.addAttribute("user_id", user_id);
            friendInterest = interestRepository.findByUserId(user_id.intValue());
            model.addAttribute("friendInterest", friendInterest);

            Follower follower = followerRepository.findByUserId(userinfo.getId());
            Blacklist blacklist = blacklistRepository.findByUserId(userinfo.getId());
            System.out.println("follower : " + follower);
            System.out.println("blacklist : " + blacklist);

            if(follower != null){
                if(follower.getFollowerUserIdList().contains(Long.toString(user_id)))
                    model.addAttribute("alreadyFollower", 1);
            }
            if(blacklist != null){
                if(blacklist.getBlackUserIdList().contains(Long.toString(user_id)))
                    model.addAttribute("alreadyBlacklist", 1);
            }

        }
        return "search/friendDetail";
    }

    @ResponseBody
    @GetMapping("/search/friendDetail/follower")
    public void follower(@RequestParam ("user_id") String user_id, @LoginUser SessionUser user, Follower follower) {
        User userinfo = userRepository.findByUsername(user.getUsername());
        Optional<Follower> followerOptional = followerRepository.findByUserIdOptional(userinfo.getId());
        System.out.println("로그인 유저 : " + userinfo);
        System.out.println("로그인 유저 팔로워 상태 : " + follower);
        User followerUser = userRepository.findById(Integer.parseInt(user_id));
        System.out.println(followerUser.getNickName() + "님에게 팔로우 신청");

        if(!followerOptional.isPresent()){
            System.out.println("Follower 객체 생성");
            follower = new Follower();
            follower.setUser(userinfo);
            follower.setFollowerUserIdList(user_id);
        }
        else{
            follower = followerRepository.findByUserId(userinfo.getId());
            System.out.println("follwerList : " + follower.getFollowerUserIdList());
            follower.setFollowerUserIdList(follower.getFollowerUserIdList() + ", " + user_id);
        }

        followerRepository.save(follower);
        System.out.println("follwer 정보 : " + follower);

    }

    @ResponseBody
    @GetMapping("/search/friendDetail/followerCancel")
    public void followerCancel(@RequestParam ("user_id") String user_id, @LoginUser SessionUser user, Follower follower) {
        User userinfo = userRepository.findByUsername(user.getUsername());
        System.out.println("로그인 유저 : " + userinfo);
        follower = followerRepository.findByUserId(userinfo.getId());
        User cancelUser = userRepository.findById(Integer.parseInt(user_id));
        System.out.println(cancelUser.getNickName() + "님 팔로우 해제");

        System.out.println("follwerList : " + follower.getFollowerUserIdList());
        if(follower.getFollowerUserIdList().contains(",")){
            if(follower.getFollowerUserIdList().substring(0, user_id.length()).equals(user_id))
                follower.setFollowerUserIdList(follower.getFollowerUserIdList().replace(user_id + ", ", ""));
            else
                follower.setFollowerUserIdList(follower.getFollowerUserIdList().replace(", " + user_id, ""));
            followerRepository.save(follower);
        }
        else
            followerRepository.delete(follower);

        System.out.println("follwer 정보 : " + follower);
    }

    @ResponseBody
    @GetMapping("/search/friendDetail/black")
    public void black(@RequestParam ("user_id") String user_id, @LoginUser SessionUser user, Blacklist blacklist) {
        User userinfo = userRepository.findByUsername(user.getUsername());
        Optional<Blacklist> blacklistOptional = blacklistRepository.findByUserIdOptional(userinfo.getId());
        System.out.println("로그인 유저 : " + userinfo);
        System.out.println("로그인 유저 블랙리스트 상태 : " + blacklist);
        User blacklistUser = userRepository.findById(Integer.parseInt(user_id));
        System.out.println(blacklistUser.getNickName() + "님을 차단");

        if(!blacklistOptional.isPresent()){
            System.out.println("Blacklist 객체 생성");
            blacklist = new Blacklist();
            blacklist.setUser(userinfo);
            blacklist.setBlackUserIdList(user_id);
        }
        else {
            blacklist = blacklistRepository.findByUserId(userinfo.getId());
            System.out.println("blacklist : " + blacklist.getBlackUserIdList());
            blacklist.setBlackUserIdList(blacklist.getBlackUserIdList() + ", " + user_id);
        }

        blacklistRepository.save(blacklist);
        System.out.println("blacklist 정보 : " + blacklist);
    }

    @ResponseBody
    @GetMapping("/search/friendDetail/blackCancel")
    public void blackCancel(@RequestParam ("user_id") String user_id, @LoginUser SessionUser user, Blacklist blacklist) {
        User userinfo = userRepository.findByUsername(user.getUsername());
        System.out.println("로그인 유저 : " + userinfo);
        blacklist = blacklistRepository.findByUserId(userinfo.getId());
        User cancelUser = userRepository.findById(Integer.parseInt(user_id));
        System.out.println(cancelUser.getNickName() + "님 차단 해제");

        System.out.println("BlackList : " + blacklist.getBlackUserIdList());
        if(blacklist.getBlackUserIdList().contains(",")){
            if(blacklist.getBlackUserIdList().substring(0, user_id.length()).equals(user_id))
                blacklist.setBlackUserIdList(blacklist.getBlackUserIdList().replace(user_id + ", ", ""));
            else
                blacklist.setBlackUserIdList(blacklist.getBlackUserIdList().replace(", " + user_id, ""));
            blacklistRepository.save(blacklist);
        }
        else
            blacklistRepository.delete(blacklist);

        System.out.println("blacklist 정보 : " + blacklist);
    }
}
