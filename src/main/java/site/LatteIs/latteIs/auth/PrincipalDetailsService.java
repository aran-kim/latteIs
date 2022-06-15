package site.LatteIs.latteIs.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.LatteIs.latteIs.web.domain.entity.User;
import site.LatteIs.latteIs.web.domain.repository.UserRepository;

import javax.servlet.http.HttpSession;

// Security config에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {
// UserDetailsService는 FormLogin시 loadUserByUsername 메소드로 로그인한 유저가 DB에 저장되어 있는 지 검색

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HttpSession httpSession;

    // Security session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        System.out.println("loadUserbyUsername 실행");
        System.out.println("user name : " + username);
        User user = userRepository.findByUsername(username);
        System.out.println("user : " + user);
        if(user == null){
            System.out.println("로그인 실패");
            return null;
        }
        else{
            userRepository.save(user);
            httpSession.setAttribute("user", new SessionUser(user));
            return new PrincipalDetails(user);
        }

    }

}
