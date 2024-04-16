package co.harborbytes.wortedeck.practicesession;

import co.harborbytes.wortedeck.practicesession.dtos.*;
import co.harborbytes.wortedeck.shared.Success;
import co.harborbytes.wortedeck.shared.SuccessPage;
import co.harborbytes.wortedeck.usermanagement.User;
import co.harborbytes.wortedeck.words.dtos.WordDTO;
import co.harborbytes.wortedeck.words.dtos.WordStatsDTO;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/practice-sessions")
public class PracticeSessionController {

    private final PracticeSessionService practiceSessionService;

    @PostMapping
    public Success<PracticeSessionDTO> createPracticeSession() {
        User loggedInUser = getLoggedInUser();
        return new Success<>(this.practiceSessionService.createPracticeSession(loggedInUser.getId()));
    }

    @GetMapping("/{id}")
    public SuccessPage<Page<WordDTO>> getPracticeSessionWords(@PathVariable("id") @Positive Long practiceSessionId, @PageableDefault(page = 0, size = 5) Pageable page) {
        return new SuccessPage<>(this.practiceSessionService.getPracticeSessionWords(practiceSessionId, page));
    }

    @PostMapping("/results")
    public Success<String> createPracticeSessionResults(@RequestBody @Validated CreatePracticeSessionResultsDTO resultsDTO) {
        return new Success<>(this.practiceSessionService.createPracticeSessionResults(getLoggedInUser().getId(), resultsDTO));
    }

    @GetMapping("/results/stats/overall")
    public Success<PracticeSessionStatsDTO> getPracticeSessionResultsOverall(){
        return new Success<>(this.practiceSessionService.getPracticeSessionStats(getLoggedInUser().getId()));
    }

    @GetMapping("/results/stats/most-difficult-words")
    public Success<List<DifficultWordDTO>> getMostDifficultWords(){
        return new Success<>(this.practiceSessionService.getMostDifficultWords(getLoggedInUser().getId()));
    }

    @GetMapping("/results/summary")
    public Success<List<PracticeSessionResultSummaryDTO>> getPracticeSessionResultsSummary(){
        return new Success<>(this.practiceSessionService.getLastFewPracticeSessionResults(getLoggedInUser().getId()));
    }

    private User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }


}
