package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.LatteIs.latteIs.web.domain.entity.User;

import java.util.List;
import java.util.Optional;

// CRUD 함수를 JpaRepository가 들고 있음
// @Repository라는 어노테이션 없어도 IoC 댐, JpaRepository를 상속하면 자동 컴포넌트
// IoC(Inversion of Control): 외부에서 의존성을 가져옴, ex) DI(Dependency Injection): 밖에서 나에게 의존성을 주입
public interface UserRepository extends JpaRepository<User, Long> {
    // findBy규칙: Username문법
    // SELECT * FROM user WHERE username = ?1
    User findByUsername(String username);

    User findByNickName(String nick_name);

    // select * from user where id = ?
    User findById(int id);

    // SELECT * FROM user WHERE provider = ?1 and providerId = ?2
    Optional<User> findByProviderAndProviderId(String provider, String providerId);


}
