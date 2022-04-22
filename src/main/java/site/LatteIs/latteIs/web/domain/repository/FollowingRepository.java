package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.Blacklist;
import site.LatteIs.latteIs.web.domain.entity.Following;

import java.util.List;

public interface FollowingRepository extends JpaRepository<Following, Long> {

    List<Following> findAllByUserId(int user_id);

    @Query(value = "select count(*) from following", nativeQuery = true)
    int countFollowingByUserId(int user_id);
}
