package co.harborbytes.wortedeck.words;

import co.harborbytes.wortedeck.shared.Success;
import co.harborbytes.wortedeck.shared.SuccessPage;
import co.harborbytes.wortedeck.usermanagement.User;
import co.harborbytes.wortedeck.words.dtos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/words")
public class WordsController {

    private final WordService wordService;

    @GetMapping("/stats/words-learnt")
    public Success<WordStatsDTO> getWordsStats(@AuthenticationPrincipal User user){
        return new Success<>(this.wordService.getWordsStats(user.getId()));
    }

//    @GetMapping(value =  "/search", params = {"word", "wordType"})
//    public SuccessPage<Page<WordVocabularyDTO>> searchVocabulary(
//            @AuthenticationPrincipal User user,
//            @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
//            @RequestParam(value = "word") String word,
//            @RequestParam(value = "wordType", required = false ) WordType wordtype){
//
//        return new SuccessPage<>(this.wordService.findWordsFromVocabulary(pageable, user.getId(), word, wordtype));
//    }

    @GetMapping(value =  "/search", params = {"word"})
    public SuccessPage<Page<WordVocabularyDTO>> searchVocabulary(
           @AuthenticationPrincipal User user,
           @PageableDefault(page = 0, size = 10, sort = "id") Pageable pageable,
           @RequestParam("word") String word,
           @RequestParam( value = "wordType", required = false) String wordTypeValue){

        WordKind wordKind = null;
        try{ wordKind = co.harborbytes.wortedeck.words.WordKind.valueOf(wordTypeValue); } catch (Exception ignored){}
        return new SuccessPage<>(this.wordService.findWordsFromVocabulary(pageable, user.getId(), word, wordKind));
    }

    @GetMapping(value = "/search/complete", params = {"word"})
    public Success<List<WordDTO>> searchKnownWords(
            @AuthenticationPrincipal User user,
            @RequestParam("word") String word){

        return new Success<>(this.wordService.findKnowWordsLike(word, user.getId()));
    }

    @GetMapping(value = "/search/raw", params = {"word"})
    public Success<List<WordDTO>> searchRawWordsLike(
            @AuthenticationPrincipal User user,
            @RequestParam("word") String word){

        return new Success<>(this.wordService.findRawWordsLike(word, user.getId()));
    }

    @PostMapping
    public Success<WordDTO> createWord(
            @AuthenticationPrincipal User user,
            @RequestBody @Validated BaseCreateWordDTO baseWord
            ){

        return new Success<>(this.wordService.saveWord(baseWord, user.getId()));
    }

    @PutMapping("/{id}")
    public Success<WordDTO> createReplaceWord(
            @AuthenticationPrincipal User user,
            @RequestBody  BaseCreateWordDTO baseWord,
            @PathVariable("id") Long wordId
    ){
       return new Success<>(this.wordService.createReplaceWord(baseWord, user.getId(), wordId));
    }
}
