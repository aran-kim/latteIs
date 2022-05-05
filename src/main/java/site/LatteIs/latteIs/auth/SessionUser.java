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

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInit(int init) {
        this.init = init;
    }
}
