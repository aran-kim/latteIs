package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.LatteIs.latteIs.web.domain.entity.Interest;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    Interest findByUserId(int user_id);

    List<Interest> findAllByMbti(String mbti);

}
