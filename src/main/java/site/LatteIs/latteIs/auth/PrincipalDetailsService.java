package site.LatteIs.latteIs.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import site.LatteIs.latteIs.domain.User;
import site.LatteIs.latteIs.domain.UserRepository;

@Service
public class PrincipalDetailsService implements UserDetailsService {
// UserDetailsService는 FormLogin시 loadUserByUsername 메소드로 로그인한 유저가 DB에 저장되어 있는 지 검색

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        System.out.println(username);
        User user = userRepository.findByUsername(username);
        System.out.println(user);
        if(user == null){
            System.out.println("로그인 실패");
            return null;
        }
        else
            return new PrincipalDetails(user);
    }

}
