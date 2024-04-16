package co.harborbytes.wortedeck.practicesession;

import co.harborbytes.wortedeck.practicesession.dtos.DifficultWordDTO;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeSessionRepository extends JpaRepository<PracticeSession, Long> {

    @Query("select new co.harborbytes.wortedeck.practicesession.WordFrequencyResult(psrd.word.id, SUM(COALESCE(CAST(psrd.rightSwipe AS integer), 0)) as rightSwipeCount) " +
            "from PracticeSessionResultDetail psrd RIGHT JOIN psrd.word w WHERE w.user.id  = :userId GROUP BY w.id ORDER BY w.id, rightSwipeCount ASC")
    List<WordFrequencyResult> findWordsByPerformance(@Param("userId") Long userId);


    @Query("SELECT p FROM PracticeSessionDetail p WHERE p.practiceSession.id = :practiceSessionId")
    Page<PracticeSessionDetail> findPracticeSessionDetails(@Param("practiceSessionId") Long practiceSessionId, Pageable page);

    @Query("SELECT COUNT(*) FROM Word w WHERE w.user.id = :userId")
    int findWordCountByUser(@Param("userId") Long userId);

    @Query("SELECT new co.harborbytes.wortedeck.practicesession.dtos.DifficultWordDTO(psrd.word.id, psrd.word.word, psrd.word.englishTranslations, COUNT(psrd.word.id) AS difficultCount) FROM PracticeSessionResultDetail psrd WHERE psrd.word.user.id = :userId AND psrd.leftSwipe = TRUE GROUP BY psrd.word.id, psrd.word.word, psrd.word.englishTranslations ORDER BY difficultCount DESC")
    List<DifficultWordDTO> findMostDifficultWordsByUser(@Param("userId") Long userId, Limit limit);

}
