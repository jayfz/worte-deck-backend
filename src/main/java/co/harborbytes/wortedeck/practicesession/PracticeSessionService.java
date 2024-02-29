package co.harborbytes.wortedeck.practicesession;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor

public class PracticeSessionService {

    private final PracticeSessionRepository practiceSessionRepository;

    public PracticeSession createPracticeSession(Long userId) {

        List<WordFrequencyResult> results = practiceSessionRepository.findWordsByPerformance(userId);

        results.forEach(result -> {
            System.out.println(String.format("word id: %s,  rightSwipeCount: %s", result.getWordId(), result.getRightSwipeCount()));
        });

        return null;
    }

}
