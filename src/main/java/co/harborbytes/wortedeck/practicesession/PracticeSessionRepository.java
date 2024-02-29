package co.harborbytes.wortedeck.practicesession;

import org.hibernate.query.QueryParameter;
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
}
