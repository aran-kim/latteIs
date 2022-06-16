package site.LatteIs.latteIs.web.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.LatteIs.latteIs.web.domain.entity.FilterWord;

import java.util.List;

public interface FilterWordRepository extends JpaRepository<FilterWord, Long> {

    List<FilterWord> findAll();
}
