package co.harborbytes.wortedeck.practicesession;

import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PracticeSessionResultRepository extends JpaRepository<PracticeSessionResult, Long> {
    List<PracticeSessionResult> findByUserId(Long userId);
    Page<PracticeSessionResult> findByUserId(Long userId, Pageable page);

    List<PracticeSessionResult> findByUserIdOrderByCreatedAtDesc(Long userId, Limit limit);
}
