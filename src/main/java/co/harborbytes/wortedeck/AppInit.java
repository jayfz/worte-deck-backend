package co.harborbytes.wortedeck;

import co.harborbytes.wortedeck.practicesession.PracticeSessionRepository;
import co.harborbytes.wortedeck.practicesession.PracticeSessionResult;
import co.harborbytes.wortedeck.practicesession.PracticeSessionResultDetail;
import co.harborbytes.wortedeck.practicesession.PracticeSessionResultRepository;
import co.harborbytes.wortedeck.usermanagement.Role;
import co.harborbytes.wortedeck.usermanagement.User;
import co.harborbytes.wortedeck.usermanagement.UserRepository;
import co.harborbytes.wortedeck.words.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class AppInit implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PracticeSessionRepository practiceSessionRepository;
    private final PracticeSessionResultRepository practiceSessionResultRepository;
    private final WordRepository wordRepository;
    private User testUser;
    private final Resource jsonTestWordsResource;
    private final ObjectMapper mapper;
    private final List<Word> testWords = new ArrayList<>();

    @Autowired
    public AppInit(final UserRepository userRepository, final PasswordEncoder passwordEncoder, final WordRepository wordRepository, final PracticeSessionRepository practiceSessionRepository, final PracticeSessionResultRepository practiceSessionResultRepository, @Value("classpath:startup.json") Resource jsonTestWordsResource, ObjectMapper mapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.practiceSessionRepository = practiceSessionRepository;
        this.practiceSessionResultRepository = practiceSessionResultRepository;
        this.jsonTestWordsResource = jsonTestWordsResource;
        this.mapper = mapper;
        this.wordRepository = wordRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("Application starting...");
        createAppTestUser();
        createAppTestWords();
        createAppPracticeSessionResults(false, false, false, true, true);
        createAppPracticeSessionResults(true, true, true, false, false);
        createAppPracticeSessionResults(false, true, false, true, false);
    }

    @Transactional
    private void createAppTestUser() {
        User user = new User();
        user.setFirstName("Carlos");
        user.setLastName("Bacca");
        user.setEmail("carlos.bacca@gmail.com");
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode("12345678"));
        this.testUser = user;
        userRepository.save(user);
    }

    @Transactional
    private void createAppTestWords() {
        List<RawWord> sampleWords = getWordsFromJson();
        sampleWords.stream().forEach((sampleWord -> {

            if (sampleWord.getType().equals(WordType.NOUN)) {
                Noun noun = new Noun();
                noun.setPlural(sampleWord.getNounPlural() == null ? "noplural" : sampleWord.getNounPlural());
                noun.setGender(sampleWord.getNounGender() == null ? NounGender.NEUTER : sampleWord.getNounGender());
                this.setBasicWordDetails(sampleWord, noun);
                this.wordRepository.save(noun);
                this.testWords.add(noun);
            }

            if (sampleWord.getType().equals(WordType.ADJECTIVE)) {
                Adjective adjective = new Adjective();
                adjective.setIsComparable(sampleWord.getAdjectiveComparable() == null ? false : sampleWord.getAdjectiveComparable());
                adjective.setComparative(sampleWord.getAdjectiveComparative());
                adjective.setSuperlative(sampleWord.getAdjectiveSuperlative());
                this.setBasicWordDetails(sampleWord, adjective);
                this.wordRepository.save(adjective);
                this.testWords.add(adjective);
            }


            if (sampleWord.getType().equals(WordType.VERB)) {
                Verb verb = new Verb();
                verb.setHasPrefix(sampleWord.getVerbWithPrefix() != null && sampleWord.getVerbWithPrefix());
                verb.setIsSeparable(sampleWord.getVerbSeparable() != null && sampleWord.getVerbSeparable());
                verb.setIsRegular(sampleWord.getVerbRegular() != null && sampleWord.getVerbRegular());
                this.setBasicWordDetails(sampleWord, verb);
                this.wordRepository.save(verb);
                this.testWords.add(verb);
            }

            if (sampleWord.getType().equals(WordType.ADVERB)) {
                Adverb adverb = new Adverb();
                this.setBasicWordDetails(sampleWord, adverb);
                this.wordRepository.save(adverb);
                this.testWords.add(adverb);
            }

            if (sampleWord.getType().equals((WordType.COMMON_EXPRESSION))) {
                CommonExpression commonExpression = new CommonExpression();
                this.setBasicWordDetails(sampleWord, commonExpression);
                this.wordRepository.save(commonExpression);
                this.testWords.add(commonExpression);
            }
        }));
    }

    private <T extends Word> void setBasicWordDetails(RawWord sampleWord, T word) {
        word.setWord(sampleWord.getWord());
        word.setType(sampleWord.getType());
        word.setPronunciations(word.getPronunciations() == null ? new String[]{} : word.getPronunciations());
        word.setEnglishTranslations(sampleWord.getEnglishTranslations() == null ? new String[]{} : sampleWord.getEnglishTranslations());
        word.setRecordingURLs(sampleWord.getRecordingURLs() == null ? new String[]{} : sampleWord.getRecordingURLs());
        word.setGermanExample("no german example added yet");
        word.setEnglishExample("no english example added yet");
        word.setGermanExampleRecordingURLs(new String[]{"none added yet"});
        word.setUser(this.testUser);
    }


    private List<RawWord> getWordsFromJson() {

        try {
            List<RawWord> sampleWords = Arrays.asList(this.mapper.readValue(this.jsonTestWordsResource.getFile(), RawWord[].class));
            return sampleWords;

        } catch (Exception exception) {
            System.out.println(String.format("failure reading startup.json file %s", exception.getMessage()));
        }
        return null;
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
}
