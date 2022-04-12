package site.LatteIs.latteIs.oauth;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import site.LatteIs.latteIs.auth.PrincipalDetails;
import site.LatteIs.latteIs.domain.Role;
import site.LatteIs.latteIs.domain.User;
import site.LatteIs.latteIs.domain.UserRepository;

import java.util.UUID;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    // userRequest 는 code를 받아서 accessToken을 응답 받은 객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest); // google의 회원 프로필 조회

        String provider = userRequest.getClientRegistration().getRegistrationId(); //google
        String providerID = oAuth2User.getAttribute("sub");
        String username = provider + "_" + providerID;

        String uuid = UUID.randomUUID().toString().substring(0, 6);
        String password = bCryptPasswordEncoder.encode("패스워드"+uuid);

        String email = oAuth2User.getAttribute("email");
        Role role = Role.ROLE_USER;

        User byUsername = userRepository.findByUsername(username);

        if(byUsername == null){
            byUsername = User.oauth2Register()
                    .username(username).password(password).email(email).role(role)
                    .provider(provider).providerID(providerID)
                    .build();
            userRepository.save(byUsername);
        }

        return new PrincipalDetails(byUsername, oAuth2User.getAttributes());
    }

}
