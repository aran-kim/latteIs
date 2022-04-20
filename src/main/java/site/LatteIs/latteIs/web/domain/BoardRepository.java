package site.LatteIs.latteIs.web.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findById(int id);

    @Override
    List<Board> findAll();

    /*@Query("SELECT board FROM board ORDER BY board.id DESC")
    List<Board> findAllDesc();*/
}
