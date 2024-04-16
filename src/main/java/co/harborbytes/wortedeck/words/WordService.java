package co.harborbytes.wortedeck.words;

import co.harborbytes.wortedeck.words.aggregation.WordStats;
import co.harborbytes.wortedeck.words.dtos.WordStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;

    public WordStatsDTO getWordsStats(Long userId) {
        Map<WordType, Integer> stats = wordRepository
                .getWordStatsForUser(userId)
                .stream()
                .collect(Collectors.toMap(WordStats::getWordType, WordStats::getCount));

        return WordStatsDTO
                .builder()
                .nounCount(stats.getOrDefault(WordType.NOUN, 0))
                .adjectiveCount(stats.getOrDefault(WordType.ADJECTIVE, 0))
                .verbCount(stats.getOrDefault(WordType.VERB, 0))
                .adverbCount(stats.getOrDefault(WordType.ADVERB, 0))
                .commonExpressionCount(stats.getOrDefault(WordType.COMMON_EXPRESSION, 0))
                .build();

    }
}
