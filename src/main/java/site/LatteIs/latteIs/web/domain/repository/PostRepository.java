package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findById(int id);
    Post findByBoardId(int board_id);
    List<Post> findAllByBoardId(int board_id);

    @Override
    List<Post> findAll();

    @Query(value = "select u.* from post p, user u where p.user_id = u.id and p.user_id = ?1", nativeQuery = true)
    Post findUsername(int id);

}
