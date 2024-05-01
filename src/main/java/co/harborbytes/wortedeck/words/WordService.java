package co.harborbytes.wortedeck.words;

import co.harborbytes.wortedeck.usermanagement.User;
import co.harborbytes.wortedeck.words.aggregation.WordStats;
import co.harborbytes.wortedeck.words.dtos.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordService {

    private final WordRepository wordRepository;
    private final DictionaryWordRepository dictionaryWordRepository;
    private final WordMapper mapper;

    public WordStatsDTO getWordsStats(Long userId) {
        Map<WordKind, Integer> stats = wordRepository
                .getWordStatsForUser(userId)
                .stream()
                .collect(Collectors.toMap(WordStats::getWordKind, WordStats::getCount));

        return WordStatsDTO
                .builder()
                .nounCount(stats.getOrDefault(co.harborbytes.wortedeck.words.WordKind.NOUN, 0))
                .adjectiveCount(stats.getOrDefault(co.harborbytes.wortedeck.words.WordKind.ADJECTIVE, 0))
                .verbCount(stats.getOrDefault(co.harborbytes.wortedeck.words.WordKind.VERB, 0))
                .adverbCount(stats.getOrDefault(co.harborbytes.wortedeck.words.WordKind.ADVERB, 0))
                .commonExpressionCount(stats.getOrDefault(co.harborbytes.wortedeck.words.WordKind.COMMON_EXPRESSION, 0))
                .build();

    }

    public Page<WordVocabularyDTO> findWordsFromVocabulary(Pageable pageable, Long userId, String search, WordKind wordKind){
        return this.wordRepository.findAllByWordIgnoreCaseAndWordTypeIsAndUserIdIs(pageable, search, wordKind, userId);
    }

    public List<WordDTO> findRawWordsLike(String search, Long userId){

        List<DictionaryWord> dictionaryWords = this.dictionaryWordRepository.findByWordContaining(search, Limit.of(5));

        List<String> wordsToMatch = dictionaryWords
                .stream()
                .map(co.harborbytes.wortedeck.words.DictionaryWord::getWord)
                .toList();

       List<Word> knownWords = this.wordRepository.findByWordInAndUserIdEquals(wordsToMatch, userId);

       for(Word knownWord : knownWords){
           dictionaryWords.removeIf(rw -> rw.getWord().equals(knownWord.getWord()));
       }

       return dictionaryWords.stream().map(this.mapper::rawWordToDTO).toList();
    }

    public List<WordDTO> findKnowWordsLike(String search, Long userId){
        List<Word> words = this.wordRepository.findKnownWordsLike(search, userId, Limit.of(5));
        return words.stream().map(this.mapper::wordToDto).toList();
    }

    @Transactional
    public WordDTO saveWord(BaseCreateWordDTO wordDTO, Long userId){
        Word word = mapper.createWordToWord(wordDTO);
        User user = new User();
        user.setId(userId);
        word.setUser(user);
        word.setReady(false);
       var result = this.wordRepository.save(word);
       return mapper.wordToDto(result);
    }

    @Transactional
    public WordDTO createReplaceWord(BaseCreateWordDTO baseWord, Long id, Long wordId) {
        Word word = mapper.createWordToWord(baseWord);
        User user = new User();
        user.setId(id);
        word.setUser(user);
        word.setId(wordId);
        var result = this.wordRepository.save(word);
        return mapper.wordToDto(result);
    }
}
