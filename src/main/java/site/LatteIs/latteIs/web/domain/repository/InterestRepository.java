package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.LatteIs.latteIs.web.domain.entity.Interest;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    Interest findByUserId(int user_id);

}
