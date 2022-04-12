package site.LatteIs.latteIs.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import site.LatteIs.latteIs.oauth.PrincipalOauth2UserService;

//@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                    .antMatchers("/user/**").authenticated() //user 주소에 대해서 인증을 요구
                    //.antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") //manager 주소는 ROLE_MANAGER 권한이나 ROLE_ADMIN 권한이 있어야 접근 가능
                    .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") //admin 주소는 ROLE_ADMIN 권한이 있어야 저근 가능
                    .anyRequest().permitAll() //나머지 주소는 인증없이 접근 가능
                .and()
                    .formLogin() // form 기반의 로그인인 경우
                        .loginPage("/loginForm") // 인증이 필요한 url에 접근하면 /logForm으로 이동
                        .usernameParameter("id") // 로그인 시 form에서 가져올 값
                        .passwordParameter("pw") // 로그인 시 form에서 가져올 값
                        .loginProcessingUrl("/login") // 로그인을 처리할 URL
                        .defaultSuccessUrl("/") // 로그인 성공 시 "/"로 이동
                        .failureUrl("/loginForm") // 로그인 실패 시 "/loginForm"으로 이동
                .and()
                    .logout() // 로그아웃할 경우
                        .logoutUrl("/logout") // 로그아웃을 처리할 URL
                        .logoutSuccessUrl("/") //로그아웃 성공 시 "/"로 이동
                .and()
                    .oauth2Login()
                        .loginPage("/loginForm")
                        .defaultSuccessUrl("/")
                        .failureUrl("/loginForm")
                        .userInfoEndpoint()
                        .userService(principalOauth2UserService);
    }
}
