package co.harborbytes.wortedeck.words;

import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DictionaryWordRepository extends JpaRepository<DictionaryWord, Integer> {

   @Query("select w from DictionaryWord w where w.word like %?#{escape([0])}%")
   List<DictionaryWord> findByWordContaining(@Param("search") String search, Limit limit);

}
