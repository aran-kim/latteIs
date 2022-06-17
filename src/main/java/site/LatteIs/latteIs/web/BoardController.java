package site.LatteIs.latteIs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
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
        return "board/board";
    }

    @GetMapping("/board/post")
    public String post(@RequestParam(value = "board_id") Long board_id, Model model, @LoginUser SessionUser user){
        if(user != null){

            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            System.out.println("받은 board_id : " + board_id);
            model.addAttribute("board_id", board_id);
            model.addAttribute("board_name", boardRepository.findById(board_id.intValue()).getSubject());

            int check_id = board_id.intValue();
            List<Post> post = postRepository.findAllByBoardId(check_id);
            System.out.println("findPostNickName Test : " + post);
            model.addAttribute("post", post);
        }
        return "board/post";
    }

    @GetMapping("/board/post/postSave")
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
        return "board/postSave";
    }

    @PostMapping("/board/post/postSaveProc")
    public String postSaveProc(@RequestParam(value = "board_id") Long board_id, Model model, @LoginUser SessionUser user, Post post){
        User user1 = userRepository.findByUsername(user.getUsername());
        Board board = boardRepository.findById(board_id.intValue());
        post.setBoard(board);
        post.setUser(user1);
        System.out.println("Post save 전 정보 : " + post);
        postRepository.save(post);
        System.out.println("Post save 후 정보 : " + post);
        return "redirect:/board/post?board_id=" + board_id;
    }

    @GetMapping("/board/post/postDetail")
    public String postDetail(@RequestParam(value = "board_id") Long board_id, @RequestParam(value = "post_id") Long post_id, Model model, @LoginUser SessionUser user, Post post){
        if(user != null){

            System.out.println("접속 아이디 : " + user.getUsername());
            System.out.println("접속 닉네임 : " + user.getNickName());
            model.addAttribute("username", user.getUsername());
            model.addAttribute("nickName", user.getNickName());

            System.out.println("받은 board_id : " + board_id);
            System.out.println("받은 post_id : " + post_id);
            model.addAttribute("board_id", board_id);
            model.addAttribute("board_name", boardRepository.findById(board_id.intValue()).getSubject());
            post = postRepository.findById(post_id.intValue());
            model.addAttribute("post", post);
            int writerId = post.getUser().getId();
            String writerNickname = userRepository.findById(writerId).getNickName();
            model.addAttribute("writername", writerNickname);
            if(writerNickname.equals(user.getNickName())){
                model.addAttribute("writerToken", 1);
                model.addAttribute("user_id", userRepository.findByUsername(user.getUsername()).getId());
            }
        }
        return "board/postDetail";
    }

    @GetMapping("/board/post/postDeleteProc")
    public String postUpdateProc(@RequestParam(value = "board_id") Long board_id, @RequestParam(value = "post_id") Long post_id, Model model, @LoginUser SessionUser user, Post post){
        User user1 = userRepository.findByUsername(user.getUsername());
        post = postRepository.findById(post_id.intValue());
        System.out.println("Post delete 전 정보 : " + post);
        postRepository.delete(post);
        return "redirect:/board/post?board_id=" + board_id;
    }
}
