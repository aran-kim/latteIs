package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.Blacklist;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {

    Blacklist findByUserId(int user_id);

    @Query(value = "select count(*) from Blacklist", nativeQuery = true)
    int countBlacklistByUserId(int user_id);
}
