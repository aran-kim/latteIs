package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.Following;

public interface FollowingRepository extends JpaRepository<Following, Long> {

    Following findByUserId(int user_id);

    @Query(value = "select count(*) from following", nativeQuery = true)
    int countFollowingByUserId(int user_id);
}
