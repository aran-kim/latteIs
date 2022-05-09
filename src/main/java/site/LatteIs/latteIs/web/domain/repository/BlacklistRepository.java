package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.Blacklist;
import site.LatteIs.latteIs.web.domain.entity.Follower;

import java.util.List;
import java.util.Optional;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    List<Blacklist> findAllByUserId(int user_id);

    @Query(value = "select * from blacklist where user_id = ?1", nativeQuery = true)
    Optional<Blacklist> findByUserIdOptional(int user_id);

    Blacklist findByUserId(int user_id);

    @Query(value = "select count(*) from Blacklist", nativeQuery = true)
    int countBlacklistByUserId(int user_id);
}
