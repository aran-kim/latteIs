package site.LatteIs.latteIs.auth;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import site.LatteIs.latteIs.domain.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
@ToString
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;
    private Map<String, Object> attributes;

    //UserDetails: Form 로그인 시 사용
    public PrincipalDetails(User user){
        this.user = user;
    }

    //OAuth2User: OAuth2 로그인 시 사용
    public PrincipalDetails(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    //UserDetails 구현
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){ // 해당 유저의 권한목록 반환

        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole().toString();
            }
        });
        return collect;
    }

    @Override
    public String getPassword(){ // 비밀번호 반환
        return user.getPassword();
    }

    @Override
    public String getUsername() { // PK값 반환
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired(){ // 계정 만료 여부
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){ // 계정 잠김 여부
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired(){ //계정 비밀번호 만료 여부
        return true;
    }

    @Override
    public boolean isEnabled(){ // 계정 활성화 여부
        return true;
    }

    @Override
    public Map<String, Object> getAttributes(){
        return attributes;
    }

    @Override
    public String getName(){
        String sub = attributes.get("sub").toString();
        return sub;
    }
}
