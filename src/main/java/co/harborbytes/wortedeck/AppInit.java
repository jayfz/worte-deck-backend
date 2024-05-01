package co.harborbytes.wortedeck;

import co.harborbytes.wortedeck.practicesession.*;
import co.harborbytes.wortedeck.practicesession.dtos.PracticeSessionDTO;
import co.harborbytes.wortedeck.usermanagement.Role;
import co.harborbytes.wortedeck.usermanagement.User;
import co.harborbytes.wortedeck.usermanagement.UserRepository;
import co.harborbytes.wortedeck.words.*;
import co.harborbytes.wortedeck.words.dtos.WordDTO;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AppInit implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PracticeSessionRepository practiceSessionRepository;
    private final PracticeSessionResultRepository practiceSessionResultRepository;
    private final DictionaryWordRepository dictionaryWordRepository;
    private final WordRepository wordRepository;
    private User testUser;
    private final Resource jsonTestWordsResource;
    private final Resource jsonRawWordsResource;
    private final ObjectMapper mapper;
    private final List<Word> testWords = new ArrayList<>();
    private final PracticeSessionService practiceSessionService;

    @Autowired
    public AppInit(final UserRepository userRepository,
                   final PasswordEncoder passwordEncoder,
                   final WordRepository wordRepository,
                   final DictionaryWordRepository dictionaryWordRepository,
                   final PracticeSessionRepository practiceSessionRepository,
                   final PracticeSessionResultRepository practiceSessionResultRepository,
                   @Value("classpath:startup.json") Resource jsonTestWordsResource,
                   @Value("classpath:parsed-and-scrapped.json") Resource rawWordsResource,
                   ObjectMapper mapper, PracticeSessionService practiceSessionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.practiceSessionRepository = practiceSessionRepository;
        this.practiceSessionResultRepository = practiceSessionResultRepository;
        this.jsonTestWordsResource = jsonTestWordsResource;
        this.jsonRawWordsResource = rawWordsResource;
        this.mapper = mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS, true);
        this.wordRepository = wordRepository;
        this.dictionaryWordRepository = dictionaryWordRepository;
        this.practiceSessionService = practiceSessionService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Application starting...");
        createAppTestUser();
        createAppTestWords();
        createAppPracticeSessionResults(false, false, false, true, true);
        createAppPracticeSessionResults(true, true, true, false, false);
        createAppPracticeSessionResults(false, true, false, true, false);
        checkWordResults();
        createAndPrintPracticeSession();
        readAndPopulateRawWordsRepository();
        System.out.println("done filling up db");
    }

    @Transactional
    private void createAppTestUser() {
        User user = new User();
        user.setFirstName("Carlos");
        user.setLastName("Bacca");
        user.setEmail("carlos.bacca@gmail.com");
        user.setResponsibility(Role.USER);
        user.setPassword(passwordEncoder.encode("12345678"));
        this.testUser = user;
        userRepository.save(user);
    }

    @Transactional
    private void createAppTestWords() {
        List<DictionaryWord> sampleWords = getWordsFromJson();
        sampleWords.stream().forEach((sampleWord -> {

            if (sampleWord.getKind().equals(co.harborbytes.wortedeck.words.WordKind.NOUN)) {
                Noun noun = new Noun();
                noun.setPlural(sampleWord.getNounPlural() == null ? "noplural" : sampleWord.getNounPlural());
                noun.setGender(sampleWord.getNounGender() == null ? NounGender.NEUTER : sampleWord.getNounGender());
                this.setBasicWordDetails(sampleWord, noun);
                this.wordRepository.save(noun);
                this.testWords.add(noun);
            }

            if (sampleWord.getKind().equals(co.harborbytes.wortedeck.words.WordKind.ADJECTIVE)) {
                Adjective adjective = new Adjective();
                adjective.setIsComparable(sampleWord.getAdjectiveComparable() == null ? false : sampleWord.getAdjectiveComparable());
                adjective.setComparative(sampleWord.getAdjectiveComparative());
                adjective.setSuperlative(sampleWord.getAdjectiveSuperlative());
                this.setBasicWordDetails(sampleWord, adjective);
                this.wordRepository.save(adjective);
                this.testWords.add(adjective);
            }


            if (sampleWord.getKind().equals(co.harborbytes.wortedeck.words.WordKind.VERB)) {
                Verb verb = new Verb();
                verb.setHasPrefix(sampleWord.getVerbWithPrefix() != null && sampleWord.getVerbWithPrefix());
                verb.setIsSeparable(sampleWord.getVerbSeparable() != null && sampleWord.getVerbSeparable());
                verb.setIsRegular(sampleWord.getVerbRegular() != null && sampleWord.getVerbRegular());
                this.setBasicWordDetails(sampleWord, verb);
                this.wordRepository.save(verb);
                this.testWords.add(verb);
            }

            if (sampleWord.getKind().equals(co.harborbytes.wortedeck.words.WordKind.ADVERB)) {
                Adverb adverb = new Adverb();
                this.setBasicWordDetails(sampleWord, adverb);
                this.wordRepository.save(adverb);
                this.testWords.add(adverb);
            }

            if (sampleWord.getKind().equals((co.harborbytes.wortedeck.words.WordKind.COMMON_EXPRESSION))) {
                CommonExpression commonExpression = new CommonExpression();
                this.setBasicWordDetails(sampleWord, commonExpression);
                this.wordRepository.save(commonExpression);
                this.testWords.add(commonExpression);
            }
        }));
    }

    private <T extends Word> void setBasicWordDetails(DictionaryWord sampleWord, T word) {
        word.setWord(sampleWord.getWord());
        word.setKind(sampleWord.getKind());
        word.setPronunciations(sampleWord.getPronunciations() == null ? new String[]{} : sampleWord.getPronunciations());
        word.setEnglishTranslations(sampleWord.getEnglishTranslations() == null ? new String[]{} : sampleWord.getEnglishTranslations());
        word.setRecordingURLs(sampleWord.getRecordingURLs() == null ? new String[]{} : sampleWord.getRecordingURLs());
        word.setGermanExample("no german example added yet");
        word.setEnglishExample("no english example added yet");
        word.setGermanExampleRecordingURLs(new String[]{"none added yet"});
        word.setReady(true);
        word.setMatches(new String[]{});
        word.setUser(this.testUser);
    }


    private List<DictionaryWord> getWordsFromJson() {

        try {
            List<DictionaryWord> sampleWords = Arrays.asList(this.mapper.readValue(this.jsonTestWordsResource.getFile(), DictionaryWord[].class));
            return sampleWords;

        } catch (Exception exception) {
            System.out.println(String.format("failure reading startup.json file %s", exception.getMessage()));
        }
        return null;
    }

    @Transactional
    private void readAndPopulateRawWordsRepository(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(this.jsonRawWordsResource.getFile()));
            List<String> lines = reader.lines().toList();
            List<DictionaryWord> dictionaryWords = new ArrayList<>();
            for(String line: lines){
                DictionaryWord dictionaryWord = this.mapper.readValue(line, DictionaryWord.class);
                dictionaryWord.setPronunciations(dictionaryWord.getPronunciations());
                dictionaryWord.setEnglishTranslations(dictionaryWord.getEnglishTranslations());
                dictionaryWords.add(dictionaryWord);
            }

            dictionaryWordRepository.saveAll(dictionaryWords);
        }
        catch (Exception exception){
            System.out.println(String.format("failure reading scrapped.json file %s, %s", exception.getMessage(), exception.getClass()));
        }
    }

    @Transactional
    private void createAppPracticeSessionResults(boolean outcome1, boolean outcome2, boolean outcome3, boolean outcome4, boolean outcome5) {
        PracticeSessionResult results = new PracticeSessionResult();
        results.setCreatedAt(Instant.now());
        results.setUser(this.testUser);
        results.setDurationInSeconds(400L);

        List<PracticeSessionResultDetail> resultDetails = new ArrayList<>();

        PracticeSessionResultDetail detail = new PracticeSessionResultDetail();
        detail.setWord(testWords.get(2));
        detail.setLeftSwipe(outcome1);
        detail.setRightSwipe(!outcome1);
        detail.setPracticeSessionResult(results);
        resultDetails.add(detail);

        detail = new PracticeSessionResultDetail();
        detail.setWord(testWords.get(4));
        detail.setLeftSwipe(outcome2);
        detail.setRightSwipe(!outcome2);
        detail.setPracticeSessionResult(results);
        resultDetails.add(detail);

        detail = new PracticeSessionResultDetail();
        detail.setWord(testWords.get(5));
        detail.setLeftSwipe(outcome3);
        detail.setRightSwipe(!outcome3);
        detail.setPracticeSessionResult(results);
        resultDetails.add(detail);

        detail = new PracticeSessionResultDetail();
        detail.setWord(testWords.get(6));
        detail.setLeftSwipe(outcome4);
        detail.setRightSwipe(!outcome4);
        detail.setPracticeSessionResult(results);
        resultDetails.add(detail);

        detail = new PracticeSessionResultDetail();
        detail.setWord(testWords.get(10));
        detail.setLeftSwipe(outcome5);
        detail.setRightSwipe(!outcome5);
        detail.setPracticeSessionResult(results);
        resultDetails.add(detail);

        results.setPracticeSessionResultDetails(resultDetails);

        resultDetails.stream().forEach(rdetail -> {
            results.setWordsTestedCount(results.getWordsTestedCount() + 1);
            results.setLeftSwipesCount(results.getLeftSwipesCount() + (rdetail.getLeftSwipe() ? 1 : 0));
            results.setRightSwipesCount(results.getRightSwipesCount() + (rdetail.getRightSwipe() ? 1 : 0));
        });

        BigDecimal score = new BigDecimal(results.getRightSwipesCount()).divide(new BigDecimal(results.getLeftSwipesCount() + results.getRightSwipesCount())).multiply(new BigDecimal(5));
        results.setScore(score);

        this.practiceSessionResultRepository.save(results);
    }

    private void checkWordResults(){
      List<WordFrequencyResult> results =  this.practiceSessionRepository.findWordsByPerformance(testUser.getId());
          results.forEach(result -> {
              System.out.println(String.format("word id: %s,  rightSwipeCount: %s", result.getWordId(), result.getRightSwipeCount()));
          });
    }

    private void createAndPrintPracticeSession(){
        final PracticeSessionDTO result = this.practiceSessionService.createPracticeSession(testUser.getId());
        Page<WordDTO> words = this.practiceSessionService.getPracticeSessionWords(result.getPracticeSessionId(), PageRequest.of(0, 40, Sort.unsorted() ));
        words.forEach((word) ->{
            System.out.println(String.format("word: %s, type: %s", word.getWord(), word.getType()));
        });

        System.out.println(words.getTotalElements());

    }
}
