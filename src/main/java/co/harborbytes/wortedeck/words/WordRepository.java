package co.harborbytes.wortedeck.words;

import co.harborbytes.wortedeck.words.aggregation.WordStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query("SELECT w.type AS wordType, COUNT(*) as count from Word w  WHERE w.user.id = :userId group by w.type")
    List<WordStats> getWordStatsForUser(@Param("userId") Long userId);

}
