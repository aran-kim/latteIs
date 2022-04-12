package site.LatteIs.latteIs.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import site.LatteIs.latteIs.config.auth.LoginUser;
import site.LatteIs.latteIs.config.auth.dto.SessionUser;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
public class IndexController {

    //private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(Model model){

        return "index";
    }

    @GetMapping("/loginHome")
    public String loginHome(){

        return "loginHome";
    }

    @GetMapping("/loginGoogle")
    public String login(){

        return "loginGoogle";
    }
}
