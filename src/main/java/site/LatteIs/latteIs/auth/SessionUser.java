package site.LatteIs.latteIs.auth;

import lombok.Getter;
import site.LatteIs.latteIs.web.domain.User;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String username;
    private String email;

    public SessionUser(User user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
    }

}
