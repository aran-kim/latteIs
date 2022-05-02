package site.LatteIs.latteIs.web.domain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import site.LatteIs.latteIs.oauth.PrincipalOauth2UserService;

@Configuration // IoC 빈(bean)을 등록
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록 (필터 체인 관리 시작 어노테이션)
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) // 특정 주소 접근시 권한 및 인증을 위한 어노테이션 활성화
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.csrf().disable(); // CSRF(Cross Site Request Forgery): 사이트간 위조 요청 (정상적인 사용자가 의도치 않은 위조요청을 보내는 것)
        http.authorizeRequests()
                    .antMatchers("/user/**").authenticated() //user 주소에 대해서 인증을 요구
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //admin 주소는 ROLE_ADMIN 권한이 있어야 접근 가능
                    .anyRequest().permitAll() //나머지 주소는 인증없이 접근 가능
                .and()
                    .formLogin() // form 기반의 로그인인 경우
                        .loginPage("/loginForm") // 인증이 필요한 url에 접근하면 /loginForm으로 이동
                        .loginProcessingUrl("/login") // "/login" 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인을 진행
                        .defaultSuccessUrl("/") // 로그인 성공 시 "/"로 이동
                        .failureUrl("/loginForm") // 로그인 실패 시 "/loginForm"으로 이동
                .and()
                    .logout() // 로그아웃할 경우
                        .logoutUrl("/logout") // 로그아웃을 처리할 URL
                        .logoutSuccessUrl("/") //로그아웃 성공 시 "/"로 이동
                .and()
                    .oauth2Login() // oauth2 기반의 로그인인 경우
                        .loginPage("/loginForm")
                        .defaultSuccessUrl("/")
                        .failureUrl("/login")
                        .userInfoEndpoint()
                        .userService(principalOauth2UserService);
    }
}
