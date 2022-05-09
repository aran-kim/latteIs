package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.MBTI;

import java.util.List;

public interface MBTIRepository extends JpaRepository<MBTI, Long> {
    MBTI findByUserId(int user_id);

    List<MBTI> findAllBymbti(String mbti);


}
