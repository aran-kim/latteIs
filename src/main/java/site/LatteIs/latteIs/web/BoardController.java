package site.LatteIs.latteIs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import site.LatteIs.latteIs.auth.LoginUser;
import site.LatteIs.latteIs.auth.SessionUser;
import site.LatteIs.latteIs.web.domain.entity.Board;
import site.LatteIs.latteIs.web.domain.entity.Post;
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.BoardRepository;
import site.LatteIs.latteIs.web.domain.repository.PostRepository;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

import java.util.List;

@Controller
public class BoardController {
    
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/board")
    public String board(Model model, @LoginUser SessionUser user){ // 나중에 변경
        if(user != null){
            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            List<Post> postList1 = postRepository.findThreeByBoardId(1);
            List<Post> postList2 = postRepository.findThreeByBoardId(2);
            List<Post> postList3 = postRepository.findThreeByBoardId(3);
            List<Post> postList4 = postRepository.findThreeByBoardId(4);

            model.addAttribute("postList1", postList1);
            model.addAttribute("postList2", postList2);
            model.addAttribute("postList3", postList3);
            model.addAttribute("postList4", postList4);
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
}
