package co.harborbytes.wortedeck.practicesession;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PracticeSessionResultRepository extends JpaRepository<PracticeSessionResult, Long> {
}
