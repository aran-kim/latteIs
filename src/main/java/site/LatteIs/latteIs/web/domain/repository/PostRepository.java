package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.LatteIs.latteIs.web.domain.entity.Post;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findById(int id);
    Post findByBoardId(int board_id);
    List<Post> findAllByBoardId(int board_id);

    @Query(value = "select * from post where board_id = ?1 order by create_date desc limit 3", nativeQuery = true)
    List<Post> findThreeByBoardId(int board_id);


    @Query(value = "select * from (\n" +
            "\tselect *, rank() over(partition by board_id order by create_date asc) as ordernum\n" +
            "    from post) as rankrow\n" +
            "    where rankrow.ordernum <= 3", nativeQuery = true)
    List<Post> findAllThreePost();

    @Override
    List<Post> findAll();

    @Query(value = "select u.* from post p, user u where p.user_id = u.id and p.user_id = ?1", nativeQuery = true)
    Post findUsername(int id);

}
