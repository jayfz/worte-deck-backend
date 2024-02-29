package co.harborbytes.wortedeck.practicesession;


import co.harborbytes.wortedeck.practicesession.dtos.PracticeSessionDTO;
import co.harborbytes.wortedeck.practicesession.dtos.PracticeSessionMapper;
import co.harborbytes.wortedeck.words.Word;
import co.harborbytes.wortedeck.words.dtos.WordDTO;
import co.harborbytes.wortedeck.words.dtos.WordMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor

public class PracticeSessionService {

    private final PracticeSessionRepository practiceSessionRepository;
    private final PracticeSessionMapper practiceSessionMapper;
    private final WordMapper wordMapper;

    @Transactional
    public PracticeSessionDTO createPracticeSession(Long userId) {
        List<WordFrequencyResult> results = practiceSessionRepository.findWordsByPerformance(userId);
        List<PracticeSessionDetail> practiceSessionWords = new ArrayList<>();
        PracticeSession practiceSession = new PracticeSession();

        results.forEach(result -> {
            if (shouldIncludeWordToPracticeSessionSimplified(result.getRightSwipeCount())) {
                PracticeSessionDetail practiceSessionDetail = new PracticeSessionDetail();
                Word word = new Word();
                word.setId(result.getWordId());
                practiceSessionDetail.setWord(word);
                practiceSessionDetail.setPracticeSession(practiceSession);
                practiceSessionWords.add(practiceSessionDetail);
            }
        });

        practiceSession.setPracticeSessionWordList(practiceSessionWords);
        this.practiceSessionRepository.save(practiceSession);
        return practiceSessionMapper.practiceSessionToDTO(practiceSession);
    }

    public Page<WordDTO> getPracticeSessionWords(Long practiceSessionId, Pageable page){
     return this.practiceSessionRepository.findPracticeSessionDetails(practiceSessionId, page).map(psd -> wordMapper.wordToDto(psd.getWord()));
    }


    private boolean shouldIncludeWordToPracticeSessionSimplified(final Long rightSwipeCount){
        if (rightSwipeCount == 0)
            return true;

        if (rightSwipeCount > 0 && rightSwipeCount <= 1) {
            return rollDice(20);
        }

        if (rightSwipeCount > 1 && rightSwipeCount <= 2) {
            return rollDice(10);
        }

        return rollDice(1);
    }
    private boolean shouldIncludeWordToPracticeSession(final Long rightSwipeCount) {
        if (rightSwipeCount >= 0 && rightSwipeCount <= 30)
            return true;

        if (rightSwipeCount > 30 && rightSwipeCount <= 60) {
            return rollDice(80);
        }

        if (rightSwipeCount > 60 && rightSwipeCount <= 90) {
            return rollDice(60);
        }

        if (rightSwipeCount > 90 && rightSwipeCount <= 120) {
            return rollDice(40);
        }

        if (rightSwipeCount > 120 && rightSwipeCount <= 150) {
            return rollDice(20);
        }

        return rollDice(10);
    }

    private boolean rollDice(long expectedChance) {
        return expectedChance > (Math.random() * 100) ;
    }

}
