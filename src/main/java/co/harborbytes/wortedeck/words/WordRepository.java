package co.harborbytes.wortedeck.words;

import co.harborbytes.wortedeck.words.aggregation.WordStats;
import co.harborbytes.wortedeck.words.dtos.WordVocabularyDTO;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query("SELECT w.kind AS wordKind, COUNT(*) as count from Word w  WHERE w.user.id = :userId group by w.kind")
    List<WordStats> getWordStatsForUser(@Param("userId") Long userId);

    @Query("Select new co.harborbytes.wortedeck.words.dtos.WordVocabularyDTO(w.id, w.word, w.englishTranslations, w.recordingURLs, w.pronunciations, w.kind) FROM Word w WHERE w.user.id = :userId AND w.word LIKE %:search% AND (w.kind = :wordKind OR :wordKind IS NULL)")
    Page<WordVocabularyDTO> findAllByWordIgnoreCaseAndWordTypeIsAndUserIdIs(Pageable pageable, @Param("search") String search, @Param("wordKind") WordKind wordKind, @Param("userId") Long userId);

    List<Word> findByWordInAndUserIdEquals(List<String> wordsToMatch, Long userId);

    @Query("SELECT w from Word w WHERE w.word LIKE %:search% AND w.user.id = :userId")
    List<Word> findKnownWordsLike( String search, @Param("userId") Long userId, Limit limit);

}
