package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.Interest;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    Interest findByUserId(int user_id);

    @Query(value = "select * from interest where user_id != ?1", nativeQuery = true)
    List<Interest> findAllExceptId(int user_id);

    List<Interest> findAllByMbti(String mbti);

    @Query(value = "select * from interest where university = ?1 and user_id != ?2", nativeQuery = true)
    List<Interest> findAllByUniversityExceptId(String university, int user_id);

    @Query(value = "select * from interest where mbti= ?1 and university = ?2 and user_id != ?3", nativeQuery = true)
    List<Interest> findAllByMbtiandUniversity(String mbti, String university, int user_id);

    @Query(value = "select * from interest where (hobby like ?1 or hobby like ?2 or hobby like ?3) and university = ?4 and user_id != ?5", nativeQuery = true)
    List<Interest> findAllByEqualInterest(String hobby1, String hobby2, String hobby3, String university, int user_id);

}
