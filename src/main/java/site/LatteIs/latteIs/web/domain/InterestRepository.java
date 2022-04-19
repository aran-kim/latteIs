package site.LatteIs.latteIs.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    Interest findByUserId(String user_id);

}
