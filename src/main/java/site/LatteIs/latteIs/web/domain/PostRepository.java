package site.LatteIs.latteIs.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Post findByBoardId(int board_id);
    List<Post> findAllByBoardId(int board_id);

    @Override
    List<Post> findAll();
}
