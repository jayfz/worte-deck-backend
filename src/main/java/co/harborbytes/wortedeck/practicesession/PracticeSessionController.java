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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Success<PracticeSessionDTO> createPracticeSession(@AuthenticationPrincipal User user) {
        return new Success<>(this.practiceSessionService.createPracticeSession(user.getId()));
    }

    @GetMapping("/{id}")
    public SuccessPage<Page<WordDTO>> getPracticeSessionWords(
            @PathVariable("id") @Positive Long practiceSessionId,
            @PageableDefault(page = 0, size = 5) Pageable page) {

        return new SuccessPage<>(this.practiceSessionService.getPracticeSessionWords(practiceSessionId, page));
    }

    @PostMapping("/results")
    public Success<String> createPracticeSessionResults(
            @AuthenticationPrincipal User user,
            @RequestBody @Validated CreatePracticeSessionResultsDTO resultsDTO) {

        return new Success<>(this.practiceSessionService.createPracticeSessionResults(user.getId(), resultsDTO));
    }

    @GetMapping("/results")
    public SuccessPage<Page<PracticeSessionResultSummaryDTO>> getPracticeSessionResultsSummaryPageable(
            @AuthenticationPrincipal User user,
            Pageable page){

        return new SuccessPage<>(this.practiceSessionService.getPracticeSessionResults(user.getId(), page));
    }

    @GetMapping("/results/stats/overall")
    public Success<PracticeSessionStatsDTO> getPracticeSessionResultsOverall(@AuthenticationPrincipal User user){
        return new Success<>(this.practiceSessionService.getPracticeSessionStats(user.getId()));
    }

    @GetMapping("/results/stats/most-difficult-words")
    public Success<List<DifficultWordDTO>> getMostDifficultWords(@AuthenticationPrincipal User user){
        return new Success<>(this.practiceSessionService.getMostDifficultWords(user.getId()));
    }

    @GetMapping("/results/summary")
    public Success<List<PracticeSessionResultSummaryDTO>> getPracticeSessionResultsSummary(@AuthenticationPrincipal User user){
        return new Success<>(this.practiceSessionService.getLastFewPracticeSessionResults(user.getId()));
    }
}
