package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.LatteIs.latteIs.web.domain.entity.MBTI;

public interface MBTIRepository extends JpaRepository<MBTI, Long> {
    MBTI findByUserId(int id);
}
