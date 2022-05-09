package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.Blacklist;
import site.LatteIs.latteIs.web.domain.entity.Follower;
import site.LatteIs.latteIs.web.domain.entity.User;

import java.util.List;
import java.util.Optional;

public interface FollowerRepository extends JpaRepository<Follower, Long> {

    List<Follower> findAllByUserId(int user_id);

    @Query(value = "select * from follower where user_id = ?1", nativeQuery = true)
    Optional<Follower> findByUserIdOptional(int user_id);

    Follower findByUserId(int user_id);

    @Query(value = "insert into follower(user_id) values (?1);", nativeQuery = true)
    void createFollower(User user);

    @Query(value = "select count(*) from follower", nativeQuery = true)
    int countFollowerByUserId(int user_id);
}
