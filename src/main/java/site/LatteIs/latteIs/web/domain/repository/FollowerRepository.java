package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.Follower;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

    Follower findByUserId(int user_id);

    @Query(value = "select count(*) from follower", nativeQuery = true)
    int countFollowerByUserId(int user_id);
}
