package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.Blacklist;
import site.LatteIs.latteIs.web.domain.entity.Follower;

import java.util.List;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

    List<Follower> findAllByUserId(int user_id);

    @Query(value = "select count(*) from follower", nativeQuery = true)
    int countFollowerByUserId(int user_id);
}
