package site.LatteIs.latteIs.auth;

import lombok.Getter;
import site.LatteIs.latteIs.web.domain.entity.User;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String username;
    private String email;
    private String nickName;
    private int init;

    public SessionUser(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.nickName = user.getNickName();
        this.init = user.getInit();
    }

}
