package co.harborbytes.wortedeck.words;

import co.harborbytes.wortedeck.shared.Success;
import co.harborbytes.wortedeck.usermanagement.User;
import co.harborbytes.wortedeck.words.dtos.WordStatsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/words")
public class WordsController {

    private final WordService wordService;


    @GetMapping("/stats/words-learnt")
    public Success<WordStatsDTO> getWordsStats(){
        User user = getLoggedInUser();
        return new Success<>(this.wordService.getWordsStats(user.getId()));
    }

    private User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
